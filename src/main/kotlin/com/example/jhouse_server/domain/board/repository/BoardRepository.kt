package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BoardRepository: JpaRepository<Board, Long>, BoardRepositoryCustom {
    abstract fun findAllByPrefixCategoryAndSavedAndUseYn(prefixCategory : PrefixCategory, saved: Boolean, useYn: Boolean, pageable: Pageable): Page<Board>
    @Query(
        nativeQuery = true,
        value = "select * from board where prefix_category = :prefixCategory",
    countQuery = "select count(*) from board")
    fun findAllByPrefixCategory(prefixCategory: String, pageable: Pageable) : Page<Board>
}