package com.example.jhouse_server.domain.intro.dto

import com.example.jhouse_server.domain.intro.entity.IntroPost
import com.example.jhouse_server.domain.intro.entity.IntroPostCategory
import java.time.LocalDate

data class IntroPostResDto(
    val postId : Long,
    val code : String,
    val title: String,
)

data class IntroPostListResDto(
    val postId : Long,
    val title : String,
    val oneLineContent : String,
    val commentCount : Int,
    val nickname : String,
    val createdAt : LocalDate,
    val imageUrl : String
)

fun toListDto(post: IntroPost) : IntroPostListResDto {
    return IntroPostListResDto(post.id, post.title, post.code, post.comment.size, post.user.nickName,
        LocalDate.of(post.createdAt.year, post.createdAt.month, post.createdAt.dayOfMonth),
        post.imageUrls[0]
    )
}

data class IntroPostCreateReqDto(
    val code : String,
    val title : String,
    val imageUrls : List<String>,
    val isSaved : Boolean,
    val category : IntroPostCategory,
)
data class IntroPostUpdateReqDto(
    val code : String,
    val title : String,
    val imageUrls : List<String>,
    val isSaved : Boolean,
    val category : String,
)
fun toDto(post : IntroPost) : IntroPostResDto {
    return IntroPostResDto(post.id, post.code, post.title)
}