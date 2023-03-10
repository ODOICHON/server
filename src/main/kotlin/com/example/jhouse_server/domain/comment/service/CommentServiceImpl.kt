package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.comment.Comment
import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.CommentUpdateReqDto
import com.example.jhouse_server.domain.comment.dto.toDto
import com.example.jhouse_server.domain.comment.repository.CommentRepository
import com.example.jhouse_server.domain.post.repository.PostRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CommentServiceImpl(
        val commentRepository: CommentRepository,
        val postRepository: PostRepository,
        val userRepository: UserRepository
) : CommentService {
    override fun getCommentAll(postId: Long): List<CommentResDto> {
        return postRepository.findByIdOrThrow(postId).comment.map { toDto(it) }
    }

    @Transactional
    override fun createComment(req: CommentCreateReqDto): CommentResDto {
        val user = userRepository.findByIdOrThrow(req.userId)
        val post = postRepository.findByIdOrThrow(req.postId)
        val comment = Comment(
                post, req.content, user
        )

        return commentRepository.save(comment).run { toDto(this) }
    }

    @Transactional
    override fun updateComment(commentId: Long, req: CommentUpdateReqDto): CommentResDto {
        val user = userRepository.findByIdOrThrow(req.userId)
        val comment = commentRepository.findByIdOrThrow(commentId)
        return if (user.equals(comment.user)) {
            comment.updateEntity(req.content).run { toDto(this) }
        } else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }
}