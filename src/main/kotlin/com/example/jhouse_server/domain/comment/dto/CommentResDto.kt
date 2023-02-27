package com.example.jhouse_server.domain.comment.dto

import com.example.jhouse_server.domain.comment.entity.Comment
import java.time.format.DateTimeFormatter

data class CommentResDto(
    val nickName: String,
    val postId: Long,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
)

data class CommentCreateReqDto(
    val postId: Long,
    val content: String,
)

data class CommentUpdateReqDto(
    val postId: Long,
    val content: String,
)

fun toDto(comment: Comment): CommentResDto {
    return CommentResDto(
        comment.user.nickName,
        comment.post.id,
        comment.content,
        comment.createdAt
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
        comment.updatedAt
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    )
}
