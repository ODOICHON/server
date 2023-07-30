package com.example.jhouse_server.domain.notification.entity

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class Notification(

    var comment: String,

    var commentUser: String,

    var status: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    var board : Board,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user : User,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {

    fun updateStatus(status: Boolean) {
        this.status = status
    }

    fun mappingUser(user: User) {
        this.user = user
        user.notifications.add(this)
    }
}