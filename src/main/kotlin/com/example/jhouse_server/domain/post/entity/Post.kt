package com.example.jhouse_server.domain.post.entity

import com.example.jhouse_server.domain.comment.Comment
import com.example.jhouse_server.domain.user.User
import com.example.jhouse_server.global.entity.BaseEntity
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import javax.persistence.*

@Entity
@Table(
        name = "post"
)
class Post(
        code : String, // 프론트엔드 측 정적 코드
        title: String, // 게시글 제목
        category: PostCategory, // 게시글 말머리
        content : String, // 게시글 내용
        imageUrls : List<String> = emptyList(), // 게시글 이미지 (최대 10장)
        address : String, // 주소 (지도 API?)
        isSaved : Boolean = false, // 저장여부( 기본 임시저장모드 false )
        user : User, // 게시글 작성자
        love : Int =0,  // 좋아요 수
        id : Long = 0L
): BaseEntity(id) {
    var code : String = code
        protected set

    var title : String = title
        protected set

    var category : PostCategory = category
        protected set

    var content : String = content
        protected set

    @Convert(converter = PostImageUrlsConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls : List<String> = imageUrls
        protected set

    var address : String = address
        protected set

    var isSaved: Boolean = isSaved
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user : User = user
        protected set

    var love : Int = love
        protected set

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comment : MutableList<Comment> = mutableListOf()


    fun updateEntity(
            code: String,
//            title: String,
            category: String,
            content: String,
            imageUrls: List<String>,
//            address : String,
            isSaved : Boolean
    ) : Post {
        this.code = code
        this.title = title
        this.category = PostCategory.values().firstOrNull {it.name == category}
                ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        this.content = content
        this.imageUrls = imageUrls
        this.address = address
        this.isSaved = isSaved
        return this
    }
}