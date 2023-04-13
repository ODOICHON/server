package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.repository.dto.AdminBoardResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardRepositoryCustom {
    fun getBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<AdminBoardResult>
}