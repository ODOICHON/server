package com.example.jhouse_server.domain.intro.entity

import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.post.entity.PostImageUrlsConverter
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import javax.persistence.*

@Entity
@Table(
    name = "intro_post"
)
class IntroPost(
    var code : String,

    var title : String,

    @Convert(converter = IntroPostCategoryConverter::class)
    var category : IntroPostCategory,

    @Convert(converter = PostImageUrlsConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls : List<String>,

    var isSaved: Boolean,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user : User,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comment : MutableList<Comment> = mutableListOf(),

    var useYn : Boolean = true,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {

    fun updateEntity(
        code: String,
        title: String,
        category: String,
        imageUrls: List<String>,
        isSaved : Boolean
    ) : IntroPost {
        this.code = code
        this.title = title
        this.category = IntroPostCategory.values().firstOrNull {it.name == category}
            ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        this.imageUrls = imageUrls
        this.isSaved = isSaved
        return this
    }

    fun deleteEntity() {
        this.useYn = false
    }
}