package com.example.jhouse_server.domain.board.repository.dto

import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.querydsl.core.annotations.QueryProjection
/**
 * =============================================================================================
 *  AdminBoardResult                 -- 쿼리 결과 응답 DTO
 *  category                         -- 게시글 카테고리
 *  title                            -- 게시글 제목
 *  id                               -- 게시글 ID
 * =============================================================================================
 * */
data class AdminBoardResult @QueryProjection constructor(
        val category: BoardCategory,
        val title: String,
        val id: Long
)
