package com.example.jhouse_server.admin.board.service

import com.example.jhouse_server.admin.board.dto.AdminBoardListResponse
import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.repository.BoardRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AdminBoardService (
        var boardRepository: BoardRepository
        ){

    fun getSearchBoardResult(adminBoardSearch: AdminBoardSearch, pageable: Pageable) : Page<AdminBoardListResponse> {
        val result = boardRepository.getBoardListWithPaging(adminBoardSearch, pageable)
        return result.map { r -> AdminBoardListResponse(r.category.value, r.title) }
    }
}