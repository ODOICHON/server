package com.example.jhouse_server.domain.comment.dto

import com.example.jhouse_server.domain.comment.entity.Comment
import java.sql.Timestamp
import java.util.*
import javax.validation.constraints.NotNull

data class CommentResDto(
    val commentId : Long,
    val nickName: String,
    val content: String,
    val createdAt: Date,
)

data class CommentReqDto(
    @field:NotNull(message = "게시글ID 필수값입니다.")
    val boardId: Long? = null,
    @field:NotNull(message = "댓글 내용은 필수값입니다.")
    val content: String? = null,
)

data class CommentUpdateReqDto(
    @field:NotNull(message = "게시글ID 필수값입니다.")
    val boardId: Long? = null,
    @field:NotNull(message = "댓글 내용은 필수값입니다.")
    val content: String? = null,
)

fun toDto(comment: Comment): CommentResDto {
    return CommentResDto(
        comment.id,
        comment.user.nickName,
        comment.content,
        Timestamp.valueOf(comment.createdAt),
    )
}
