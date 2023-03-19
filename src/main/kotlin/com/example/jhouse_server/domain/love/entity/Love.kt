package com.example.jhouse_server.domain.love.entity

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(
    name = "love"
)
class Love(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    var board: Board,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) :BaseEntity() {
}