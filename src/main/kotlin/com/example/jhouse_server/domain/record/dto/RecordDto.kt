package com.example.jhouse_server.domain.record.dto

import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record_review.dto.RecordReviewResDto
import com.example.jhouse_server.domain.record_review_apply.dto.RecordReviewApplyResDto
import com.querydsl.core.annotations.QueryProjection
import org.springframework.data.domain.Page
import java.time.LocalDateTime

data class RecordReqDto(
    //JsonProperty, validation 추가
    val title: String,
    val content: String,
    val part: String,
    val category: String,
    val dType: String
)

data class RecordUpdateDto(
    //JsonProperty, validation 추가
    val title: String,
    val content: String
)

data class RecordResDto(
    val recordId: Long,
    val title: String,
    val content: String,
    val hits: Int,
    val part: String,
    val dType: String,
    val category: String,
    val nickName: String,
    val createAt: LocalDateTime
)

data class RecordWithReviewResDto(
    val recordId: Long,
    val title: String,
    val content: String,
    val hits: Int,
    val part: String,
    val nickName: String,
    val createAt: LocalDateTime,
    val reviews: List<RecordReviewResDto>,
    val reviewers: List<RecordReviewApplyResDto>
)

data class RecordPageCondition(
    val part: String,
    val dType: String,
    val category: String
)

data class RecordReviewCondition(
    val status: String?
)

data class RecordThumbnailResDto @QueryProjection constructor(
    val recordId: Long,
    val title: String,
    val content: String,
    val nickName: String,
    val createAt: LocalDateTime,
    val part: Part
)

data class RecordPageResDto(
    val records: Page<RecordThumbnailResDto>
)

data class RecordHotThumbnailResDto @QueryProjection constructor(
    val recordId: Long,
    val title: String
)

data class RecordHotResDto(
    val records: List<RecordHotThumbnailResDto>
)