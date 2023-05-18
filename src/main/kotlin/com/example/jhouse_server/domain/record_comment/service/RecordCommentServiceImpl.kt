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
            ref = parent.ref
            val parentStep: Long = parent.step
            val parentAllChildrenSize: Long = parent.allChildrenSize
            step = parentStep + parentAllChildrenSize + 1
            level = parent.level + 1
            var grandParent = parent
            while(grandParent != null) {    //n + 1
                grandParent!!.updateAllChildrenSize()
                grandParent = grandParent!!.parent
            }
            recordCommentRepository.updateStep(step, record, ref)
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
}