package com.example.jhouse_server.domain.record_review.dto

import java.time.LocalDateTime

data class RecordReviewReqDto(
    //JsonProperty, validation 추가
    val recordId: Long,
    val content: String,
    val status: String
)

data class RecordReviewUpdateDto(
    //JsonProperty, validation 추가
    val content: String,
    val status: String
)

data class RecordReviewResDto(
    val recordId: Long,
    val content: String,
    val status: String,
    val nickName: String,
    val createAt: LocalDateTime
)