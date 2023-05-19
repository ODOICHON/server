package com.example.jhouse_server.domain.record_comment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime
import javax.validation.constraints.NotBlank

data class RecordCommentReqDto(
    @JsonProperty("record_id")
    @field:NotBlank
    val recordId: Long,
    @JsonProperty("parent_id")
    val parentId: Long?,
    @field:NotBlank(message = "댓글은 1자 이상 입력해주세요.")
    val content: String
)

data class RecordCommentUpdateDto(
    @field:NotBlank(message = "댓글은 1자 이상 입력해주세요.")
    val content: String
)

data class RecordCommentResDto @QueryProjection constructor(
    @JsonProperty("comment_id")
    val commentId: Long,
    val level: Long,
    val content: String,
    @JsonProperty("nick_name")
    val nickName: String,
    @JsonFormat(pattern = "MM.dd.yyyy")
    @JsonProperty("create_at")
    val createAt: LocalDateTime
)
