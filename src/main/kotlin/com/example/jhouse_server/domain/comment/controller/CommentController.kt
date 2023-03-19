package com.example.jhouse_server.domain.comment.controller // ktlint-disable package-name

import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.CommentUpdateReqDto
import com.example.jhouse_server.domain.comment.service.CommentService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    val commentService: CommentService,
) {

    @GetMapping("/{boardId}")
    fun getCommentAll(
        @PathVariable boardId: Long,
    ): ApplicationResponse<List<CommentResDto>> {
        return ApplicationResponse.ok(commentService.getCommentAll(boardId))
    }

    @PostMapping
    fun createComment(
        @RequestBody @Validated req: CommentCreateReqDto,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(commentService.createComment(req, user))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody @Validated req: CommentUpdateReqDto,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(commentService.updateComment(commentId, req, user))
    }

    @DeleteMapping("{/commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @AuthUser user: User
    ) :ApplicationResponse<Nothing> {
        commentService.deleteComment(commentId, user);
        return ApplicationResponse.ok();
    }
}
