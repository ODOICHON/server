package com.example.jhouse_server.domain.post.dto

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory

data class PostResDto(
        val postId : Long,
        val code : String
)

data class PostCreateReqDto(
        val userId : Long,
        val code : String,
        val title : String,
        val content : String,
        val imageUrls : List<String>,
        val address : String,
        val isSaved : Boolean,
        val category : PostCategory,
)

data class PostUpdateReqDto(
        val code: String,
        val content : String,
        val imageUrls: List<String>,
        val category : String,
        val isSaved : Boolean
)

fun toDto(post : Post) : PostResDto {
    return PostResDto(post.id, post.code)
}