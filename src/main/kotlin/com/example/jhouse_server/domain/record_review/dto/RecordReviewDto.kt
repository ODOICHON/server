package com.example.jhouse_server.domain.record_review.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class RecordReviewReqDto(
    @JsonProperty("record_id")
    val recordId: Long,
    @field:NotBlank(message = "리뷰는 1자 이상 입력해주세요.")
    val content: String,
    @field:NotBlank
    val status: String
)

data class RecordReviewUpdateDto(
    @field:NotBlank(message = "리뷰는 1자 이상 입력해주세요.")
    val content: String,
    @field:NotBlank
    val status: String
)

data class RecordReviewResDto(
    @JsonProperty("record_id")
    val recordId: Long,
    val content: String,
    val status: String,
    @JsonProperty("nick_name")
    val nickName: String,
    @JsonFormat(pattern = "MM.dd.yyyy")
    @JsonProperty("create_at")
    val createAt: LocalDateTime
)