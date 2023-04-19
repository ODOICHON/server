package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.repository.dto.AdminBoardResult
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardRepositoryCustom {
    fun getBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<AdminBoardResult>
    fun getBoardAllWithKeyword(name: PrefixCategory, keyword: String, pageable: Pageable): Page<Board>
}