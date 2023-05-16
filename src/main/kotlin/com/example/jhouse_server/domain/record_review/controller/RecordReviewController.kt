package com.example.jhouse_server.domain.record_review.controller

import com.example.jhouse_server.domain.record_review.dto.RecordReviewReqDto
import com.example.jhouse_server.domain.record_review.dto.RecordReviewUpdateDto
import com.example.jhouse_server.domain.record_review.service.RecordReviewService
import com.example.jhouse_server.domain.user.entity.Authority.ADMIN
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
@RequestMapping("/api/v1/record_review")
class RecordReviewController(
    private val recordReviewService: RecordReviewService
) {

    @Auth(ADMIN)
    @PostMapping
    fun saveRecordReview(
        @RequestBody recordReviewReqDto: RecordReviewReqDto,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordReviewService.saveRecordReview(recordReviewReqDto, user))
    }

    @Auth(ADMIN)
    @PutMapping("/{review_id}")
    fun updateRecordReview(
        @RequestBody recordReviewUpdateDto: RecordReviewUpdateDto,
        @AuthUser user: User,
        @PathVariable("review_id") reviewId: Long
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordReviewService.updateRecordReview(recordReviewUpdateDto, user, reviewId))
    }

    @Auth(ADMIN)
    @DeleteMapping("/{review_id}")
    fun deleteRecordReview(
        @AuthUser user: User,
        @PathVariable("review_id") reviewId: Long
    ): ApplicationResponse<Nothing> {
        recordReviewService.deleteRecordReview(user, reviewId)
        return ApplicationResponse.ok()
    }
}