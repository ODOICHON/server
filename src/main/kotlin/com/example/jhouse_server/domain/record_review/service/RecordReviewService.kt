package com.example.jhouse_server.domain.record_review.service

import com.example.jhouse_server.domain.record_review.dto.RecordReviewReqDto
import com.example.jhouse_server.domain.record_review.dto.RecordReviewUpdateDto
import com.example.jhouse_server.domain.user.entity.User

interface RecordReviewService {

    fun saveRecordReview(recordReviewReqDto: RecordReviewReqDto, user: User): Long

    fun updateRecordReview(recordReviewUpdateDto: RecordReviewUpdateDto, user: User, reviewId: Long): Long

    fun deleteRecordReview(user: User, reviewId: Long)
}