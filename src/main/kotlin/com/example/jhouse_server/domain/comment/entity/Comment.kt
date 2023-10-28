package com.example.jhouse_server.domain.comment.entity

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(
    name = "comment"
)
class Comment(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    var board : Board,

    var content : String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user : User,

    @Id @GeneratedValue
    val id : Long = 0L,
): BaseEntity() {
    /**
     * =============================================================================================
     *  댓글 수정
     * =============================================================================================
     * */
    fun updateEntity(content : String) : Comment {
        this.content = content
        return this
    }
}