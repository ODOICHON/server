package com.example.jhouse_server.domain.record_comment.controller

import com.example.jhouse_server.domain.record_comment.dto.RecordCommentReqDto
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentUpdateDto
import com.example.jhouse_server.domain.record_comment.service.RecordCommentService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/record_comment")
class RecordCommentController(
    private val recordCommentService: RecordCommentService
) {
    @Auth
    @PostMapping
    fun saveRecordComment(
        @RequestBody recordCommentReqDto: RecordCommentReqDto,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordCommentService.saveRecordComment(recordCommentReqDto, user))
    }

    @Auth
    @PutMapping("/{comment_id}")
    fun updateRecordComment(
        @RequestBody recordCommentUpdateDto: RecordCommentUpdateDto,
        @AuthUser user: User,
        @PathVariable("comment_id") commentId: Long
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordCommentService.updateRecordComment(recordCommentUpdateDto, user, commentId))
    }

    @Auth
    @DeleteMapping("/{comment_id}")
    fun deleteRecordComment(
        @AuthUser user: User,
        @PathVariable("comment_id") commentId: Long
    ): ApplicationResponse<Nothing> {
        recordCommentService.deleteRecordComment(user, commentId)
        return ApplicationResponse.ok()
    }
}