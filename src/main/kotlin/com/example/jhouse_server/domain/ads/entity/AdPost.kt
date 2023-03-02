package com.example.jhouse_server.domain.ads.entity

import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.post.entity.PostImageUrlsConverter
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
    name = "ads_post"
)
class AdPost(
    var code: String,
    var title: String,
    @Convert(converter = AdsPostCategoryConverter::class)
    var category: AdsPostCategory,

    @Convert(converter = PostImageUrlsConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls: List<String>,

    var isSaved: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    var love: Int = 0,

    var isFixed: Boolean = false,

    var isFixedAt: LocalDateTime? = null,

    @OneToMany(mappedBy = "post", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comment: MutableList<Comment> = mutableListOf(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
): BaseEntity() {

    fun updateEntity(
        code: String,
        title: String,
        category: String,
        imageUrls: List<String>,
        isSaved: Boolean
    ) : AdPost {
        this.code = code
        this.title = title
        this.category = AdsPostCategory.values().firstOrNull { it.name == category }
            ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        this.imageUrls = imageUrls
        this.isSaved = isSaved
        return this
    }

    fun updateFixed() : AdPost {
        this.isFixed = true
        this.isFixedAt  = LocalDateTime.now()
        return this
    }

    fun updateLove() : AdPost {
        this.love ++;
        return this
    }
}