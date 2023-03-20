package com.example.jhouse_server.domain.board.entity

import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.PrefixCategoryConverter
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.love.entity.Love
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
    name = "board"
)
class Board(
    var title: String,
    var code: String,
    var content : String,
    @Convert(converter = BoardCategoryConverter::class)
    var category : BoardCategory,
    @Convert(converter = BoardImageUrlConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls : List<String>,
    var saved: Boolean = true,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user : User,
    @Convert(converter = PrefixCategoryConverter::class)
    var prefixCategory : PrefixCategory,
    var fixed : Boolean = false,
    @LastModifiedDate
    @Column(updatable = true)
    var fixedAt : LocalDateTime? = null,
    var useYn : Boolean = true,
    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    var love : MutableList<Love> = mutableListOf(),
    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comment : MutableList<Comment> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {

    fun updateEntity(
        title: String,
        code: String,
        content: String,
        category: String,
        imageUrls: List<String>,
        saved: Boolean,
        prefixCategory: String
    ) : Board {
        this.title = title
        this.code = code
        this.content = content
        this.category = BoardCategory.values().firstOrNull { it.name == category }
            ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        this.imageUrls = imageUrls
        this.saved = saved
        this.prefixCategory = PrefixCategory.values().firstOrNull { it.name == prefixCategory }
            ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        return this
    }
    fun updateFixed() : Board {
        this.fixed = true
        this.fixedAt = LocalDateTime.now()
        return this
    }
    fun addLove(love: Love) : Board{
        this.love.add(love)
        return this
    }
    fun addComment(comment: Comment) {
        this.comment.add(comment)
    }
    fun deleteEntity() {
        this.useYn = false
    }

    fun deleteLove(love: Love): Board {
        this.love.remove(love)
        return this
    }
}