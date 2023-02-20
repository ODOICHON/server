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
        var code : String,

        var title : String,

//        @Enumerated(EnumType.STRING)
        @Convert(converter = PostCategoryConverter::class)
        var category : PostCategory,

        var content : String,

        @Convert(converter = PostImageUrlsConverter::class) // 이미지 url ","로 슬라이싱
        var imageUrls : List<String>,

        var address : String,

        var isSaved: Boolean,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        var user : User,

        var love : Int = 0,

        @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
        var comment : MutableList<Comment> = mutableListOf(),

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long = 0L
): BaseEntity() {


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