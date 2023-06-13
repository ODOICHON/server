package com.example.jhouse_server.domain.board.service

import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.entity.BoardCode
import com.example.jhouse_server.domain.board.repository.BoardCodeRepository
import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
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
    val boardRepository: BoardRepository,
    val boardCodeRepository: BoardCodeRepository
): BoardService {

    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun createBoard(req: BoardReqDto, user: User): Long {
        val content = getContent(req.code!!)
        val fixed = if(req.prefixCategory == PrefixCategory.ADVERTISEMENT) req.fixed!! else false
        val code = boardCodeRepository.save(BoardCode(req.code))
        val board = Board(
            req.title!!, content, req.category!!, req.imageUrls, user, req.prefixCategory!!, fixed, code
        )
        return boardRepository.save(board).id
    }

    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun updateBoard(boardId: Long, req: BoardUpdateReqDto, user: User): Long {
        val board = boardRepository.findByIdOrThrow(boardId)
        if (user != board.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        val content = getContent(req.code!!)
        var code = boardCodeRepository.findByIdOrThrow(board.boardCode.id)
        code.updateEntity(req.code)
        return board.updateEntity(
            req.title!!,
            code,
            content,
            req.category!!,
            req.imageUrls,
            req.prefixCategory!!
        ).id
    }

    override fun getBoardAll(boardListDto: BoardListDto, pageable: Pageable): Page<BoardResDto> {
        return boardRepository.getBoardAll(boardListDto, pageable)
                .map { toListDto(it) }
    }

    @Cacheable(key="#boardPreviewListDto.toString()", cacheManager = "cacheManager", value= ["board"])
    override fun getBoardPreviewAll(boardPreviewListDto: BoardPreviewListDto): List<BoardResDto> {
        return boardRepository.getBoardPreviewAll(boardPreviewListDto).map { toListDto(it) }
    }

    override fun getBoardOne(boardId: Long): BoardResOneDto {
        return boardRepository.findByIdOrThrow(boardId).run {
            toDto(this)
        }
    }

    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun deleteBoard(boardId: Long, user: User) {
        val board = boardRepository.findByIdOrThrow(boardId)
        if(user == board.user || user.authority == Authority.ADMIN) board.deleteEntity()
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    override fun getCategory(name: String): List<CodeResDto> {
        return BoardCategory.values().filter { it.superCategory.name == name }.map { CodeResDto(it.value, it.name) }
    }


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
}