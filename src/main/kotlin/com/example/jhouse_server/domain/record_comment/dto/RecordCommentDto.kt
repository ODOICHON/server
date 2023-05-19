package com.example.jhouse_server.domain.record_comment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class RecordCommentReqDto(
    val recordId: Long,
    val parentId: Long?,
    val content: String
)

data class RecordCommentUpdateDto(
    val content: String
)

data class RecordCommentResDto @QueryProjection constructor(
    val commentId: Long,
    val level: Long,
    val content: String,
    val nickName: String,
    @JsonFormat(pattern = "MM.dd.yyyy")
    val createAt: LocalDateTime
)
