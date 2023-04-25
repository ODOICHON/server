package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BoardCodeRepository : JpaRepository<BoardCode, Long> {

}
