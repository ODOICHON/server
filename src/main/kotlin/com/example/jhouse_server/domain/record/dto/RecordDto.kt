package com.example.jhouse_server.domain.record.dto

import com.example.jhouse_server.domain.record_comment.dto.RecordCommentResDto
import com.example.jhouse_server.domain.record_review.dto.RecordReviewResDto
import com.example.jhouse_server.domain.record_review_apply.dto.RecordReviewApplyResDto
import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.querydsl.core.annotations.QueryProjection
import org.springframework.data.domain.Page
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank
/**
 * =============================================================================================
 * REQUEST DTO
 * =============================================================================================
 */
data class RecordReqDto(
    @field:NotBlank(message = "제목은 1자 이상 입력해주세요.")
    val title: String,
    @field:NotBlank(message = "내용은 1자 이상 입력해주세요.")
    val content: String,
    @field:NotBlank
    val part: String,
    @field:NotBlank
    val category: String,
    @field:NotBlank
    val type: String
)

data class RecordUpdateDto(
    @field:NotBlank(message = "제목은 1자 이상 입력해주세요.")
    val title: String,
    @field:NotBlank(message = "내용은 1자 이상 입력해주세요.")
    val content: String
)

data class RecordPageCondition(
    val part: String,
    val type: String,
    val category: String
)

data class RecordReviewCondition(
    val status: String?
)

/**
 * =============================================================================================
 * RESPONSE DTO
 * =============================================================================================
 */
data class RecordResDto(
    @JsonProperty("record_id")
    val recordId: Long,
    val title: String,
    val content: String,
    val hits: Int,
    val part: String,
    val type: String,
    val category: String,
    @JsonProperty("nick_name")
    val nickName: String,
    @JsonFormat(pattern = "MM.dd.yyyy")
    @JsonProperty("create_at")
    val createAt: LocalDateTime,
    val comments: Page<RecordCommentResDto>
)

data class RecordWithReviewResDto(
    @JsonProperty("record_id")
    val recordId: Long,
    val title: String,
    val content: String,
    val hits: Int,
    val part: String,
    @JsonProperty("nick_name")
    val nickName: String,
    @JsonFormat(pattern = "MM.dd.yyyy")
    @JsonProperty("create_at")
    val createAt: LocalDateTime,
    val reviews: List<RecordReviewResDto>,
    val reviewers: List<RecordReviewApplyResDto>
)

data class RecordPageResDto(
    val records: Page<RecordThumbnailResDto>
)

data class RecordHotResDto(
    @JsonProperty("records")
    val records: List<RecordHotThumbnailResDto>
)

/**
 * =============================================================================================
 * QUERY RESULT VO
 * =============================================================================================
 */
data class RecordThumbnailResDto @QueryProjection constructor(
    @JsonProperty("record_id")
    val recordId: Long,
    val title: String,
    val content: String,
    @JsonProperty("nick_name")
    val nickName: String,
    @JsonFormat(pattern = "MM.dd.yyyy")
    @JsonProperty("create_at")
    val createAt: LocalDateTime,
    val part: String
)

data class RecordHotThumbnailResDto @QueryProjection constructor(
    @JsonProperty("record_id")
    val recordId: Long,
    @JsonProperty("title")
    val title: String
)