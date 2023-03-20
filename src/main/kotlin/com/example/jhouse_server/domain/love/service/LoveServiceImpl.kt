package com.example.jhouse_server.domain.love.service

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.love.entity.Love
import com.example.jhouse_server.domain.love.repository.LoveRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class LoveServiceImpl(
    val boardRepository: BoardRepository,
    val loveRepository: LoveRepository
) : LoveService {

    @Transactional
    override fun loveBoard(postId: Long, user: User): Long {
        val board = boardRepository.findByIdOrThrow(postId)
        if (loveRepository.existsByBoardAndUser(board, user)) {
            throw ApplicationException(ErrorCode.ALREADY_LOVE)
        }
        val love = Love(
            user, board
        )
        loveRepository.save(love)
        return board.addLove(love).id
    }

    @Transactional
    override fun hateBoard(postId: Long, user: User) {
        val board = boardRepository.findByIdOrThrow(postId)
        val love = loveRepository.findByUserAndBoard(user, board)
        board.deleteLove(love)
        loveRepository.deleteById(love.id)
    }
}