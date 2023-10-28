package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.comment.dto.CommentReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.toDto
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.comment.repository.CommentRepository
import com.example.jhouse_server.domain.notification.entity.Notification
import com.example.jhouse_server.domain.notification.repository.NotificationRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class CommentServiceImpl(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
        val commentRepository: CommentRepository,
        val boardRepository: BoardRepository,
        val notificationRepository: NotificationRepository
) : CommentService {
    /**
     * =============================================================================================
     *  댓글 전체 조회
     * =============================================================================================
     * */
    override fun getCommentAll(postId: Long): List<CommentResDto> {
        return boardRepository.findByIdOrThrow(postId).comment.map { toDto(it) }
    }
    /**
     * =============================================================================================
     *  댓글 작성
     * =============================================================================================
     * */
    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun createComment(req: CommentReqDto, user: User): Long {
        // (1) 게시글 조회
        val board = boardRepository.findByIdOrThrow(req.boardId)
        // (2) 댓글 생성
        val comment = Comment(
                board, req.content!!, user
        )
        // (3) 댓글 저장
        commentRepository.save(comment)
        // (4) 게시글 작성자가 아닌 경우
        if(user != board.user) {
            // (5) 알림 데이터 생성
            val notification = Notification(comment.content, user.nickName, false, board, board.user)
            // (6) 알림 데이터 저장
            notificationRepository.save(notification)
            notification.mappingUser(board.user)
        }

        return comment.id
    }
    /**
     * =============================================================================================
     *  댓글 수정
     * =============================================================================================
     * */
    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun updateComment(commentId: Long, req: CommentReqDto, user: User): Long {
        val comment = commentRepository.findByIdOrThrow(commentId)
        return if (user == comment.user) {
            comment.updateEntity(req.content!!).id
        } else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }
    /**
     * =============================================================================================
     *  댓글 삭제
     * =============================================================================================
     * */
    @CacheEvict(allEntries = true, cacheManager = "cacheManager", value = ["board"])
    @Transactional
    override fun deleteComment(commentId: Long, user: User) {
        val comment = commentRepository.findByIdOrThrow(commentId)
        if (user == comment.user || user.authority == Authority.ADMIN) commentRepository.delete(comment)
        else throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
    }
}