package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface BoardRepository: JpaRepository<Board, Long>, BoardRepositoryCustom {
    abstract fun findAllByPrefixCategoryAndUseYn(prefixCategory : PrefixCategory, useYn: Boolean, pageable: Pageable): Page<Board>

    fun findByFixedAndPrefixCategoryAndUseYn(fixed: Boolean, prefixCategory: PrefixCategory, useYn : Boolean): List<Board>

    @Query("select b from Board b where b.id in (:ids)")
    fun findByIds(@Param("ids") ids: List<Long>) : List<Board>

    fun countByPrefixCategoryAndUseYn(prefixCategory: PrefixCategory, useYn: Boolean) : Long

    fun countByUseYn(useYn: Boolean): Long

    @EntityGraph(attributePaths = ["boardCode"])
    fun findByIdAndUseYn(id: Long, useYn: Boolean): Optional<Board>

    @Modifying(clearAutomatically = true)
    @Query("delete from Board b where b.id in (:ids)")
    fun deleteByIds(@Param("ids") ids: List<Long>): Int

}