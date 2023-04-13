package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long>, BoardRepositoryCustom {
    abstract fun findAllByPrefixCategoryAndUseYn(prefixCategory : PrefixCategory, useYn: Boolean, pageable: Pageable): Page<Board>
}