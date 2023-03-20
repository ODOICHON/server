package com.example.jhouse_server.domain.love.repository

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.love.entity.Love
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface LoveRepository : JpaRepository<Love, Long> {
    fun findByUserAndBoard(user: User, board: Board): Love
    fun existsByBoardAndUser(board: Board, user: User): Boolean
}