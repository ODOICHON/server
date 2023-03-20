package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.CommentUpdateReqDto
import com.example.jhouse_server.domain.comment.dto.toDto
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.comment.repository.CommentRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CommentServiceImpl(
        val commentRepository: CommentRepository,
        val boardRepository: BoardRepository,
) : CommentService {
    override fun getCommentAll(postId: Long): List<CommentResDto> {
        return boardRepository.findByIdOrThrow(postId).comment.map { toDto(it) }
    }

    @Transactional
    override fun createComment(req: CommentCreateReqDto, user: User): Long {
        val board = boardRepository.findByIdOrThrow(req.boardId)
        val comment = Comment(
                board, req.content!!, user
        )
        return commentRepository.save(comment).id
    }

    @Transactional
    override fun updateComment(commentId: Long, req: CommentUpdateReqDto, user: User): Long {
        val comment = commentRepository.findByIdOrThrow(commentId)
        return if (user == comment.user) {
            comment.updateEntity(req.content!!).id
        } else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    @Transactional
    override fun deleteComment(commentId: Long, user: User) {
        val comment = commentRepository.findByIdOrThrow(commentId)
        if (user == comment.user)  {
           commentRepository.delete(comment)
        } else throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
    }
}