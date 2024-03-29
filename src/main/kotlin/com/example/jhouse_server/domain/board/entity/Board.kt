package com.example.jhouse_server.domain.board.entity

import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.PrefixCategoryConverter
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.love.entity.Love
import com.example.jhouse_server.domain.notification.entity.Notification
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(
    name = "board", indexes = [Index(name = "idx__prefixCategory__category", columnList = "category, prefixCategory, useYn")]
)
class Board(
    @Column(length = 50)
    var title: String,

    @Column(length = Int.MAX_VALUE)
    var content : String,

    @Convert(converter = BoardCategoryConverter::class)
    @Column(length = 20)
    var category : BoardCategory,

    @Column(length = Int.MAX_VALUE)
    @Convert(converter = BoardImageUrlConverter::class) // 이미지 url ","로 슬라이싱
    var imageUrls : List<String>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user : User,

    @Convert(converter = PrefixCategoryConverter::class)
    @Column(length = 20)
    var prefixCategory : PrefixCategory,

    var fixed : Boolean = false,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL]) @JoinColumn(name = "board_code_id")
    var boardCode: BoardCode,

    @Column(updatable = true)
    var fixedAt : LocalDateTime? = null,

    var useYn : Boolean = true,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    var love : MutableList<Love> = mutableListOf(),

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    var comment : MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "board", cascade = [CascadeType.ALL], orphanRemoval = true)
    var notification : MutableList<Notification> = mutableListOf(),

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {
    /**
     * =============================================================================================
     *  게시글 수정
     * =============================================================================================
     * */
    fun updateEntity(
        title: String,
        code: BoardCode,
        content: String,
        category: String,
        imageUrls: List<String>,
        prefixCategory: String
    ) : Board {
        this.title = title
        this.boardCode = code
        this.content = content
        this.category = BoardCategory.values().firstOrNull { it.name == category }
            ?: BoardCategory.EMPTY
        this.imageUrls = imageUrls
        this.prefixCategory = PrefixCategory.values().firstOrNull { it.name == prefixCategory }
            ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        return this
    }
    /**
     * =============================================================================================
     *  게시글 상단 고정
     * =============================================================================================
     * */
    fun updateFixed(status: Boolean) {
        this.fixed = status
        if (status) this.fixedAt = LocalDateTime.now()
        else this.fixedAt = null
    }
    /**
     * =============================================================================================
     *  게시글 논리 삭제
     * =============================================================================================
     * */
    fun deleteEntity() {
        this.useYn = false
    }
    /**
     * =============================================================================================
     *  1 : N 관계 테이블
     * =============================================================================================
     * */
    fun addLove(love: Love) : Board{
        this.love.add(love)
        return this
    }
    fun deleteComment(comment: Comment) {
        this.comment.remove(comment)
    }
    fun addComment(comment: Comment) {
        this.comment.add(comment)
    }
    fun deleteLove(love: Love): Board {
        this.love.remove(love)
        return this
    }
}