package com.example.jhouse_server.domain.board.repository.dto

import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.querydsl.core.annotations.QueryProjection

data class AdminBoardResult @QueryProjection constructor(
        val category: BoardCategory,
        val title: String,
        val id: Long
)
