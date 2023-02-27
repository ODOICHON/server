package com.example.jhouse_server.domain.post.dto

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
import java.time.LocalDate

data class PostResDto(
        val postId : Long,
        val code : String,
        val title: String,
)

data class PostListResDto(
        val postId : Long,
        val title : String,
        val oneLineContent : String,
        val commentCount : Int,
        val nickname : String,
        val createdAt : LocalDate,
        val imageUrl : String
)

fun toListDto(post: Post) : PostListResDto {
        return PostListResDto(post.id, post.title, post.code, post.comment.size, post.user.nickName,
                LocalDate.of(post.createdAt.year, post.createdAt.month, post.createdAt.dayOfMonth),
                post.imageUrls[0]
        )
}

data class PostCreateReqDto(
        val code : String,
        val title : String,
        val imageUrls : List<String>,
        val isSaved : Boolean,
        val category : PostCategory,
)

data class PostUpdateReqDto(
        val code: String,
        val title : String,
        val imageUrls: List<String>,
        val category : String,
        val isSaved : Boolean
)

fun toDto(post : Post) : PostResDto {
    return PostResDto(post.id, post.code, post.title)
}