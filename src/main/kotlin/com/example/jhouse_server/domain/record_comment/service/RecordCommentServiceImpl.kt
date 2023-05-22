package com.example.jhouse_server.domain.record_comment.service

import com.example.jhouse_server.domain.record.repository.RecordRepository
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentReqDto
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentUpdateDto
import com.example.jhouse_server.domain.record_comment.entity.RecordComment
import com.example.jhouse_server.domain.record_comment.repository.RecordCommentRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RecordCommentServiceImpl(
    private val recordRepository: RecordRepository,
    private val recordCommentRepository: RecordCommentRepository
): RecordCommentService {

    private val DELETE_MESSAGE = "삭제된 댓글입니다."

    @Transactional
    override fun saveRecordComment(recordCommentReqDto: RecordCommentReqDto, user: User): Long {
        val record = recordRepository.findByIdOrThrow(recordCommentReqDto.recordId)
        var parent: RecordComment? = null
        val refCount = recordCommentRepository.findNextRef(record)
        var ref: Long = if(refCount == null) 1 else (refCount + 1).toLong()
        var step: Long = 1
        var level: Long = 1
        var allChildrenSize: Long = 0
        if(recordCommentReqDto.parentId != null) {
            parent = recordCommentRepository.findByIdOrThrow(recordCommentReqDto.parentId)

            val parentStep: Long = parent.step
            val parentAllChildrenSize: Long = parent.allChildrenSize
            step = parentStep + parentAllChildrenSize + 1
            ref = parent.ref
            level = parent.level + 1

            val comments = recordCommentRepository.findSameRef(record, ref)
            updateAllChildrenSize(parent)
            updateStep(comments, step)
        }
        return recordCommentRepository.save(RecordComment(recordCommentReqDto.content, record, user, ref, step, level, allChildrenSize, parent)).id
    }

    @Transactional
    override fun updateRecordComment(recordCommentUpdateDto: RecordCommentUpdateDto, user: User, commentId: Long): Long {
        val recordComment = recordCommentRepository.findByIdOrThrow(commentId)
        validateUser(recordComment, user)
        recordComment.updateRecordComment(recordCommentUpdateDto)
        return recordComment.id
    }

    @Transactional
    override fun deleteRecordComment(user: User, commentId: Long) {
        val recordComment = recordCommentRepository.findByIdOrThrow(commentId)
        validateUser(recordComment, user)
        recordComment.updateRecordComment(RecordCommentUpdateDto(DELETE_MESSAGE))
    }

    private fun validateUser(recordComment: RecordComment, user: User) {
        if(user != recordComment.user) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        }
    }

    private fun updateAllChildrenSize(parent: RecordComment?) {
        var grandParent = parent
        while(grandParent != null) {
            grandParent!!.updateAllChildrenSize()
            grandParent = grandParent!!.parent
        }
    }

    private fun updateStep(comments: List<RecordComment>, step: Long) {
        comments.forEach {
            if(it.step >= step) {
                it.updateStep()
            }
        }
    }
}
