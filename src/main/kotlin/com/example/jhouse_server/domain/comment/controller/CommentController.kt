package com.example.jhouse_server.domain.comment.controller // ktlint-disable package-name

import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.CommentUpdateReqDto
import com.example.jhouse_server.domain.comment.service.CommentService
import com.example.jhouse_server.global.response.ApplicationResponse
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

    @GetMapping("/{postId}")
    fun getCommentAll(
        @PathVariable postId: Long,
    ): ApplicationResponse<List<CommentResDto>> {
        return ApplicationResponse.ok(commentService.getCommentAll(postId))
    }

    @PostMapping
    fun createComment(
        @RequestBody req: CommentCreateReqDto,
    ): ApplicationResponse<CommentResDto> {
        return ApplicationResponse.ok(commentService.createComment(req))
    }

    @PutMapping("/{commentId}")
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody req: CommentUpdateReqDto,
    ): ApplicationResponse<CommentResDto> {
        return ApplicationResponse.ok(commentService.updateComment(commentId, req))
    }
}
