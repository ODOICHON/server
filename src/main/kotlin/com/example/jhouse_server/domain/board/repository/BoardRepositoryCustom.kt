package com.example.jhouse_server.domain.board.repository

import com.example.jhouse_server.admin.board.dto.AdminBoardSearch
import com.example.jhouse_server.domain.board.BoardListDto
import com.example.jhouse_server.domain.board.BoardPreviewListDto
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.board.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardRepositoryCustom {
    fun getFixableBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board>
    fun getDeletableBoardListWithPaging(adminBoardSearch: AdminBoardSearch, pageable: Pageable): Page<Board>
    fun getBoardAll(boardListDto: BoardListDto, pageable: Pageable): Page<BoardResDto>
    fun getBoardPreviewAll(boardPreviewListDto: BoardPreviewListDto): List<Board>

}