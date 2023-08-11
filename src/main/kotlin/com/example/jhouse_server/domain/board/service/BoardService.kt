package com.example.jhouse_server.domain.board.service

import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.board.dto.BoardMyPageResDto
import com.example.jhouse_server.domain.board.dto.BoardResDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardService {
    fun createBoard(req: BoardReqDto, user: User): Long
    fun updateBoard(boardId: Long, req: BoardUpdateReqDto, user: User): Long
//    fun getBoardAll(category: String, pageable: Pageable): Page<BoardResDto>
    fun getBoardAll(boardListDto: BoardListDto, pageable: Pageable): Page<BoardResDto>
    fun getBoardPreviewAll(boardPreviewListDto: BoardPreviewListDto): List<BoardResDto>
    fun getBoardOne(boardId: Long): BoardResOneDto
    fun deleteBoard(boardId: Long, user: User)
    fun getCategory(name: String): List<CodeResDto>
    fun getUserBoardAll(user: User, pageable: Pageable): Page<BoardMyPageResDto>
    fun getUserCommentAll(user: User, pageable: Pageable): Page<BoardMyPageResDto>
    fun getUserLoveAll(user: User, pageable: Pageable): Page<BoardMyPageResDto>
}