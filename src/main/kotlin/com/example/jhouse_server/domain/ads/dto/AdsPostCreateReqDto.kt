package com.example.jhouse_server.domain.ads.dto

import com.example.jhouse_server.domain.ads.entity.AdPost
import com.example.jhouse_server.domain.ads.entity.AdsPostCategory
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class AdsPostCreateReqDto(
    @field:NotNull(message = "code는 필수값입니다.")
    val code : String? = null,
    @field:NotNull(message = "게시글의 제목은 필수값입니다.")
    val title : String? = null,
    val imageUrls : List<String>,
    //@field:NotNull(message = "임시 저장 여부는 필수값입니다.")
    val isSaved : Boolean? = null,
    @field:NotNull(message = "말머리는 필수값입니다.")
    val category : AdsPostCategory? = null,
)

data class AdsPostUpdateReqDto(
    @field:NotNull(message = "code는 필수값입니다.")
    val code : String? = null,
    @field:NotNull(message = "게시글의 제목은 필수값입니다.")
    val title : String? = null,
    val imageUrls : List<String>,
//    @field:NotNull(message = "임시 저장 여부는 필수값입니다.")
    val isSaved : Boolean? = null,
    @field:NotNull(message = "말머리는 필수값입니다.")
    val category : String? = null,
)

data class AdsPostResDto(
    val postId : Long,
    val code : String,
    val isFixed : Boolean,
    val love : Int,
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
    val love : Int
)

fun toDto(adPost: AdPost) : AdsPostResDto {
    return AdsPostResDto(adPost.id, adPost.code, adPost.isFixed, adPost.love)
}

fun toListDto(post: AdPost) : AdsPostListResDto {
    return AdsPostListResDto(post.id, post.title, post.code, post.comment.size, post.user.nickName,
        LocalDate.of(post.createdAt.year, post.createdAt.month, post.createdAt.dayOfMonth),
        post.imageUrls[0], post.isFixed, post.love
    )
}