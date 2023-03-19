package com.example.jhouse_server.domain.intro.dto

import com.example.jhouse_server.domain.intro.entity.IntroPost
import com.example.jhouse_server.domain.intro.entity.IntroPostCategory
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

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
    @field:NotNull(message = "code는 필수값입니다.")
    val code : String? = null,
    @field:NotNull(message = "게시글의 제목은 필수값입니다.")
    val title : String? = null,
    val imageUrls : List<String>,
    @field:NotNull(message = "임시 저장 여부는 필수값입니다.")
    val isSaved : Boolean? = null,
    @field:NotNull(message = "말머리는 필수값입니다.")
    val category : IntroPostCategory? = null,
)
data class IntroPostUpdateReqDto(
    @field:NotNull(message = "code는 필수값입니다.")
    val code : String? = null,
    @field:NotNull(message = "게시글의 제목은 필수값입니다.")
    val title : String? = null,
    val imageUrls : List<String>,
    @field:NotNull(message = "임시 저장 여부는 필수값입니다.")
    val isSaved : Boolean? = null,
    @field:NotNull(message = "말머리는 필수값입니다.")
    val category : String? = null,
)
fun toDto(post : IntroPost) : IntroPostResDto {
    return IntroPostResDto(post.id, post.code, post.title)
}