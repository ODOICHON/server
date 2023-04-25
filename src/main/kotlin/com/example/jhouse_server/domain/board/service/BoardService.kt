package com.example.jhouse_server.domain.board.service

import com.example.jhouse_server.domain.board.*
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface BoardService {
    fun createBoard(req: BoardReqDto, user: User): Long
    fun updateBoard(boardId: Long, req: BoardUpdateReqDto, user: User): Long
    fun getBoardAll(category: String, pageable: Pageable): Page<BoardResDto>
    fun getBoardOne(boardId: Long): BoardResOneDto
    fun deleteBoard(boardId: Long, user: User)
//    fun fixBoard(boardId: Long, user: User): Long
    fun getCategory(name: String): List<CodeResDto>
    fun getBoardAllWithKeyword(name: String, keyword: String, pageable: Pageable): Page<BoardResDto>

}