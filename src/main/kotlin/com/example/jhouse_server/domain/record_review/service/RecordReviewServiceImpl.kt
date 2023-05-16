package com.example.jhouse_server.domain.record_review.service

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.record_review.dto.RecordReviewReqDto
import com.example.jhouse_server.domain.record_review.dto.RecordReviewUpdateDto
import com.example.jhouse_server.domain.record_review.entity.RecordReview
import com.example.jhouse_server.domain.record_review.entity.RecordReviewStatus
import com.example.jhouse_server.domain.record_review.repository.RecordReviewRepository
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApply
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApplyStatus
import com.example.jhouse_server.domain.record_review_apply.repository.RecordReviewApplyRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class RecordReviewServiceImpl(
    private val recordReviewRepository: RecordReviewRepository,
    private val recordReviewApplyRepository: RecordReviewApplyRepository,
    private val recordService: RecordService
): RecordReviewService {

    @Transactional
    override fun saveRecordReview(recordReviewReqDto: RecordReviewReqDto, user: User): Long {
        val recordReviewApply = recordReviewApplyRepository.findWithRecord(recordReviewReqDto.recordId, user.id)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION) }
        validateRecordReviewApplyStatus(recordReviewApply)
        val record = recordReviewApply.record
        val recordReview = recordReviewRepository.save(RecordReview(recordReviewReqDto.content,
            RecordReviewStatus.getStatus(recordReviewReqDto.status), recordReviewApply, record, user))
        updateRecordReviewApplyStatus(record, recordReview, recordReviewApply)
        return recordReview.id
    }

    @Transactional
    override fun updateRecordReview(recordReviewUpdateDto: RecordReviewUpdateDto, user: User, reviewId: Long): Long {
        val recordReview = recordReviewRepository.findWithUserAndRecordAndApply(reviewId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION) }
        val recordReviewApply = recordReview.apply
        val record = recordReview.record
        validateUser(recordReview, user)
        validateRecordReviewApplyStatus(recordReviewApply)
        recordReview.updateRecordReview(recordReviewUpdateDto)
        updateRecordReviewApplyStatus(record, recordReview, recordReviewApply)
        return recordReview.id
    }

    @Transactional
    override fun deleteRecordReview(user: User, reviewId: Long) {
        val recordReview = recordReviewRepository.findWithUserAndApply(reviewId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION) }
        val recordReviewApply = recordReview.apply
        validateUser(recordReview, user)
        validateRecordReviewApplyStatus(recordReviewApply)
        recordReviewRepository.delete(recordReview)
    }

    private fun validateUser(recordReview: RecordReview, user: User) {
        if(user != recordReview.reviewer) {
            throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        }
    }

    private fun validateRecordReviewApplyStatus(recordReviewApply: RecordReviewApply) {
        if (recordReviewApply.status == RecordReviewApplyStatus.APPROVE) {
            throw ApplicationException(ErrorCode.ALREADY_APPROVE)
        }
    }

    private fun updateRecordReviewApplyStatus(record: Record, recordReview: RecordReview, recordReviewApply: RecordReviewApply) {
        when (recordReview.status) {
            RecordReviewStatus.APPROVE -> recordReviewApply.updateStatus(RecordReviewApplyStatus.APPROVE)
            RecordReviewStatus.REJECT -> recordReviewApply.updateStatus(RecordReviewApplyStatus.REJECT)
            RecordReviewStatus.MINE -> {}
        }
        recordService.updateRecordStatus(record)
    }
}