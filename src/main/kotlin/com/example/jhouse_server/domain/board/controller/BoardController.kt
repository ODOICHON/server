package com.example.jhouse_server.domain.board.controller

import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(
    val boardService: BoardService
) {
    /**
     * 게시글 작성하기
     *
     * @author 이은비
     * */
    @Auth
    @PostMapping
    fun createBoard(
        @RequestBody @Validated req: BoardReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(boardService.createBoard(req, user))
    }

    @Auth
    @PutMapping("/{boardId}")
    fun updateBoard(
        @PathVariable boardId : Long,
        @RequestBody @Validated req: BoardUpdateReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(boardService.updateBoard(boardId, req, user))
    }

    @GetMapping
    fun getBoardAll(
        @RequestParam category: String,
        pageable: Pageable
    ) : ApplicationResponse<Page<BoardResDto>> {
        return ApplicationResponse.ok(boardService.getBoardAll(category, pageable))
    }

    @GetMapping("/{boardId}")
    fun getBoardOne(
        @PathVariable boardId: Long
    ) : ApplicationResponse<BoardResOneDto> {
        return ApplicationResponse.ok(boardService.getBoardOne(boardId))
    }

    @Auth
    @DeleteMapping("/{boardId}")
    fun deleteBoard(
        @PathVariable boardId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        boardService.deleteBoard(boardId, user)
        return ApplicationResponse.ok()
    }

    @Auth(Authority.ADMIN)
    @PutMapping("/fix/{boardId}")
    fun fixBoard(
        @PathVariable boardId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(boardService.fixBoard(boardId, user))
    }

    @GetMapping("/category")
    fun getCategory(
        @RequestParam name : String
    ) : ApplicationResponse<List<CodeResDto>> {
        return ApplicationResponse.ok(boardService.getCategory(name))
    }
}