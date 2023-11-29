package com.example.jhouse_server.domain.board.service

import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.dto.BoardMyPageResDto
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.entity.BoardCode
import com.example.jhouse_server.domain.board.repository.BoardCodeRepository
import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.board.repository.dto.CustomPageImpl
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.text.Normalizer
import java.util.regex.Matcher
import java.util.regex.Pattern

@Service
@Transactional(readOnly = true)
class BoardServiceImpl(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
    val boardRepository: BoardRepository,
    val boardCodeRepository: BoardCodeRepository
): BoardService {
    /**
     * =============================================================================================
     *  게시글 생성       -- REDIS 캐시 적용
     *  @param req
     *  @param user
     * =============================================================================================
     * */
    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun createBoard(req: BoardReqDto, user: User): Long {
        // (1) 순수 게시글 내용 추출
        val content = getContent(req.code!!)
        // (2) 상단 고정 여부 확인
        val fixed = if(req.prefixCategory == PrefixCategory.ADVERTISEMENT) req.fixed!! else false
        // (3) 게시글 내용 원본 데이터 저장
        val code = boardCodeRepository.save(BoardCode(req.code))
        // (4) 게시글 카테고리 탐색 ( 없을 경우, EMPTY 지정 )
        val category = BoardCategory.values().firstOrNull { it == req.category } ?: BoardCategory.EMPTY
        // (5) 게시글 데이터 생성
        val board = Board(
            req.title!!, content, category, req.imageUrls, user, req.prefixCategory!!, fixed, code
        )
        // (6) 게시글 저장
        return boardRepository.save(board).id
    }
    /**
     * =============================================================================================
     *  게시글 수정       -- REDIS 캐시 적용
     *  @param req
     *  @param user
     * =============================================================================================
     * */
    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun updateBoard(boardId: Long, req: BoardUpdateReqDto, user: User): Long {
        // (1) 게시글 조회
        val board = boardRepository.findByIdOrThrow(boardId)
        // (2) 수정 권한 확인
        if (user != board.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        // (3) 순수 게시글 내용 추출
        val content = getContent(req.code!!)
        // (4) 게시글 원본 데이터 조회
        var code = boardCodeRepository.findByIdOrThrow(board.boardCode.id)
        // (5) 게시글 원본 데이터 덮어 쓰기
        code.updateEntity(req.code)
        // (6) 게시글 수정
        return board.updateEntity(
            req.title!!,
            code,
            content,
            req.category!!,
            req.imageUrls,
            req.prefixCategory!!
        ).id
    }
    /**
     * =============================================================================================
     *  게시글 목록 조회       -- REDIS 캐시 적용
     *  @param boardListDto
     *  @param pageable
     * =============================================================================================
     * */
//    @Cacheable(key="#boardListDto.toString()+#pageable.pageNumber.toString()", cacheManager = "cacheManager", value = ["board"])
    override fun getBoardAll(boardListDto: BoardListDto, pageable: Pageable): Page<BoardResDto> {
        val boardAll = boardRepository.getBoardAll(boardListDto, pageable).map { toListDto(it) }
        return CustomPageImpl(boardAll.content, boardAll.number, boardAll.size, boardAll.totalElements)
    }
    /**
     * =============================================================================================
     *  게시글 목록 조회 ( 메인페이지 )       -- REDIS 캐시 적용
     *  @param boardPreviewListDto
     * =============================================================================================
     * */
//    @Cacheable(key="#boardPreviewListDto.toString()", cacheManager = "cacheManager", value= ["board"])
    override fun getBoardPreviewAll(boardPreviewListDto: BoardPreviewListDto): List<BoardResDto> {
        return boardRepository.getBoardPreviewAll(boardPreviewListDto).map { toListDto(it) }
    }
    /**
     * =============================================================================================
     *  게시글 상세 조회
     *  @param boardId
     * =============================================================================================
     * */
    override fun getBoardOne(boardId: Long): BoardResOneDto {
        return boardRepository.findByIdOrThrow(boardId).run {
            toDto(this)
        }
    }
    /**
     * =============================================================================================
     *  게시글 삭제       -- REDIS 캐시 적용
     *  @param boardId
     *  @param user
     * =============================================================================================
     * */
    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun deleteBoard(boardId: Long, user: User) {
        val board = boardRepository.findByIdOrThrow(boardId)
        if(user == board.user || user.authority == Authority.ADMIN) board.deleteEntity()
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }
    /**
     * =============================================================================================
     *  게시글 카테고리 조회
     *  @param name
     * =============================================================================================
     * */
    override fun getCategory(name: String): List<CodeResDto> {
        return BoardCategory.values().filter { it.superCategory.name == name }.map { CodeResDto(it.value, it.name) }
    }
    /**
     * =============================================================================================
     *  내가 작성한 게시글 목록 조회 ( 마이페이지 )
     *  @param user
     *  @param pageable
     * =============================================================================================
     * */
    override fun getUserBoardAll(user: User, pageable: Pageable): Page<BoardMyPageResDto> {
        val boards = boardRepository.getUserBoardAll(user, pageable)
        return CustomPageImpl(boards.content, boards.number, boards.size, boards.totalElements)
    }
    /**
     * =============================================================================================
     *  내가 작성한 댓글의 게시글 목록 조회 ( 마이페이지 )
     *  @param user
     *  @param pageable
     * =============================================================================================
     * */
    override fun getUserCommentAll(user: User, pageable: Pageable): Page<BoardMyPageResDto> {
        val boards = boardRepository.getUserCommentAll(user, pageable)
        return CustomPageImpl(boards.content, boards.number, boards.size, boards.totalElements)
    }
    /**
     * =============================================================================================
     *  내가 좋아요한 게시글 목록 조회 ( 마이페이지 )
     *  @param user
     *  @param pageable
     * =============================================================================================
     * */
    override fun getUserLoveAll(user: User, pageable: Pageable): Page<BoardMyPageResDto> {
        val boards = boardRepository.getUserLoveAll(user, pageable)
        return CustomPageImpl(boards.content, boards.number, boards.size, boards.totalElements)
    }
}

/**
 * =============================================================================================
 *  PUBLIC FUNCTION
 * =============================================================================================
 * */

/**
 * =============================================================================================
 *  순수 게시글 추출
 * =============================================================================================
 * */
fun getContent(code: String): String {
    var str = code
    str = Normalizer.normalize(str, Normalizer.Form.NFKC)
    var mat: Matcher

    // <script> 파싱
    val script = Pattern.compile("<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL)
    mat = script.matcher(str)
    str = mat.replaceAll("")

    // <style> 파싱
    val style = Pattern.compile("<style[^>]*>.*</style>", Pattern.DOTALL)
    mat = style.matcher(str)
    str = mat.replaceAll("")

    // <tag> 파싱
    val tag = Pattern.compile("<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>")
    mat = tag.matcher(str)
    str = mat.replaceAll("")

    // ntag 파싱
    val ntag = Pattern.compile("<\\w+\\s+[^<]*\\s*>")
    mat = ntag.matcher(str)
    str = mat.replaceAll("")

    // entity ref 처리
    val entity = Pattern.compile("&[^;]+;")
    mat = entity.matcher(str)
    str = mat.replaceAll("")

    // whitespace 처리
    val wspace = Pattern.compile("\\s\\s+")
    mat = wspace.matcher(str)
    str = mat.replaceAll("")
    return str
}