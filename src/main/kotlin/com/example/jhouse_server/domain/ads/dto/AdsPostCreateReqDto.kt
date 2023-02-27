package com.example.jhouse_server.domain.ads.dto

import com.example.jhouse_server.domain.ads.entity.AdPost
import com.example.jhouse_server.domain.ads.entity.AdsPostCategory
import java.time.LocalDate

data class AdsPostCreateReqDto(
    val code : String,
    val title : String,
    val imageUrls: List<String>,
    val isSaved : Boolean,
    val category : AdsPostCategory,
)

data class AdsPostUpdateReqDto(
    val code : String,
    val title : String,
    val imageUrls: List<String>,
    val isSaved : Boolean,
    val category : String,
)

data class AdsPostResDto(
    val postId : Long,
    val code : String,
    val isFixed : Boolean,
)

data class AdsPostListResDto(
    val postId : Long,
    val title : String,
    val oneLineContent : String,
    val commentCount : Int,
    val nickname : String,
    val createdAt : LocalDate,
    val imageUrl : String,
    val isFixed : Boolean,
)

fun toDto(adPost: AdPost) : AdsPostResDto {
    return AdsPostResDto(adPost.id, adPost.code, adPost.isFixed)
}

fun toListDto(post: AdPost) : AdsPostListResDto {
    return AdsPostListResDto(post.id, post.title, post.code, post.comment.size, post.user.nickName,
        LocalDate.of(post.createdAt.year, post.createdAt.month, post.createdAt.dayOfMonth),
        post.imageUrls[0], post.isFixed
    )
}