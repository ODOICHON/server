package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.domain.board.entity.BoardCode
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCodeRepository : JpaRepository<BoardCode, Long> {

}
