package com.example.jhouse_server.domain.board.controller

import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.dto.BoardMyPageResDto
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    /**
     * =============================================================================================
     *  DI for Service
     * =============================================================================================
     * */
    val boardService: BoardService
) {
    /**
     * =============================================================================================
     *  게시글 생성
     *  @param req
     *  @param user
     * =============================================================================================
     * */
    @Auth
    @PostMapping
    fun createBoard(
        @RequestBody @Validated req: BoardReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(boardService.createBoard(req, user))
    }
    /**
     * =============================================================================================
     *  게시글 수정
     *  @param boardId
     *  @param req
     *  @param user
     * =============================================================================================
     * */
    @Auth
    @PutMapping("/{boardId}")
    fun updateBoard(
        @PathVariable boardId : Long,
        @RequestBody @Validated req: BoardUpdateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(boardService.updateBoard(boardId, req, user))
    }
    /**
     * =============================================================================================
     *  게시글 목록 조회          -- 커뮤니티 게시글 검색&조건&조회
     *  @param boardListDto
     *  @param pageable
     * =============================================================================================
     * */
    @GetMapping
    fun getBoardAll(
            @ModelAttribute boardListDto: BoardListDto,
            @PageableDefault(size=8, page=0) pageable: Pageable
    ): ApplicationResponse<Page<BoardResDto>> {
        return ApplicationResponse.ok(boardService.getBoardAll(boardListDto, pageable))
    }
    /**
     * =============================================================================================
     *  게시글 목록 조회          -- 메인페이지 ( INTRO | COMMUNITY )
     *  @param boardPreviewListDto
     * =============================================================================================
     * */
    @GetMapping("/preview")
    fun getBoardAllPreview(@ModelAttribute boardPreviewListDto: BoardPreviewListDto): ApplicationResponse<List<BoardResDto>>{
        return ApplicationResponse.ok(boardService.getBoardPreviewAll(boardPreviewListDto))
    }
    /**
     * =============================================================================================
     *  게시글 상세 조회
     *  @param boardId
     * =============================================================================================
     * */
    @GetMapping("/{boardId}")
    fun getBoardOne(
        @PathVariable boardId: Long
    ) : ApplicationResponse<BoardResOneDto> {
        return ApplicationResponse.ok(boardService.getBoardOne(boardId))
    }
    /**
     * =============================================================================================
     *  게시글 삭제
     *  @param boardId
     *  @param user
     * =============================================================================================
     * */
    @Auth
    @DeleteMapping("/{boardId}")
    fun deleteBoard(
        @PathVariable boardId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        boardService.deleteBoard(boardId, user)
        return ApplicationResponse.ok()
    }
    /**
     * =============================================================================================
     *  게시글 카테고리 조회
     *  @param name
     * =============================================================================================
     * */
    @GetMapping("/category")
    fun getCategory(
        @RequestParam name : String
    ) : ApplicationResponse<List<CodeResDto>> {
        return ApplicationResponse.ok(boardService.getCategory(name))
    }
    /**
     * =============================================================================================
     *  내가 작성한 게시글 목록 조회  -- 마이페이지
     *  @param user
     *  @param pageable
     * =============================================================================================
     * */
    @Auth
    @GetMapping("/my")
    fun getUserBoardAll(
        @AuthUser user: User,
        @PageableDefault(size=10, page=0) pageable: Pageable
    ): ApplicationResponse<Page<BoardMyPageResDto>> {
        return ApplicationResponse.ok(boardService.getUserBoardAll(user, pageable))
    }
    /**
     * =============================================================================================
     *  내가 작성한 댓글의 게시글 목록 조회 -- 마이페이지
     *  @param user
     *  @param pageable
     * =============================================================================================
     * */
    @Auth
    @GetMapping("/my/comment")
    fun getUserCommentAll(
        @AuthUser user: User,
        @PageableDefault(size=10, page=0) pageable: Pageable
    ): ApplicationResponse<Page<BoardMyPageResDto>> {
        return ApplicationResponse.ok(boardService.getUserCommentAll(user, pageable))
    }
    /**
     * =============================================================================================
     *  내가 좋아요한 게시글 목록 조회 -- 마이페이지
     *  @param user
     *  @param pageable
     * =============================================================================================
     * */
    @Auth
    @GetMapping("/my/love")
    fun getUserLoveAll(
        @AuthUser user: User,
        @PageableDefault(size=10, page=0) pageable: Pageable
    ): ApplicationResponse<Page<BoardMyPageResDto>> {
        return ApplicationResponse.ok(boardService.getUserLoveAll(user, pageable))
    }
}