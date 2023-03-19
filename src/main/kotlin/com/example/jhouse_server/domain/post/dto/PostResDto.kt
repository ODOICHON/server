package com.example.jhouse_server.domain.post.dto

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
import java.time.LocalDate
import javax.validation.constraints.NotNull

data class PostResDto(
        val postId : Long,
        val code : String,
        val title: String,
        val love : Int
)

data class PostListResDto(
        val postId : Long,
        val title : String,
        val oneLineContent : String,
        val commentCount : Int,
        val nickname : String,
        val createdAt : LocalDate,
        val imageUrl : String,
        val love : Int
)

fun toListDto(post: Post) : PostListResDto {
        return PostListResDto(post.id, post.title, post.code, post.comment.size, post.user.nickName,
                LocalDate.of(post.createdAt.year, post.createdAt.month, post.createdAt.dayOfMonth),
                post.imageUrls[0], post.love
        )
}

data class PostCreateReqDto(
        @field:NotNull(message = "code는 필수값입니다.")
        val code : String? = null,
        @field:NotNull(message = "게시글의 제목은 필수값입니다.")
        val title : String? = null,
        val imageUrls : List<String>,
        @field:NotNull(message = "임시 저장 여부는 필수값입니다.")
        val isSaved : Boolean? = null,
        @field:NotNull(message = "말머리는 필수값입니다.")
        val category : PostCategory? = null,
)

data class PostUpdateReqDto(
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

fun toDto(post : Post) : PostResDto {
    return PostResDto(post.id, post.code, post.title, post.love)
}

data class CodeResDto(
        val code : String,
        val name : String
)