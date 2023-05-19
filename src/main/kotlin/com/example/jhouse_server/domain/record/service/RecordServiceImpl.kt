package com.example.jhouse_server.domain.record.service

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.entity.odori.Odori
import com.example.jhouse_server.domain.record.entity.odori.OdoriCategory
import com.example.jhouse_server.domain.record.entity.retrospection.Retrospection
import com.example.jhouse_server.domain.record.entity.retrospection.RetrospectionCategory
import com.example.jhouse_server.domain.record.entity.technology.Technology
import com.example.jhouse_server.domain.record.entity.technology.TechnologyCategory
import com.example.jhouse_server.domain.record.repository.RecordRepository
import com.example.jhouse_server.domain.record.repository.odori.OdoriRepository
import com.example.jhouse_server.domain.record.repository.retrospection.RetrospectionRepository
import com.example.jhouse_server.domain.record.repository.technology.TechnologyRepository
import com.example.jhouse_server.domain.record_comment.repository.RecordCommentRepository
import com.example.jhouse_server.domain.record_review.dto.RecordReviewResDto
import com.example.jhouse_server.domain.record_review.repository.RecordReviewRepository
import com.example.jhouse_server.domain.record_review_apply.dto.RecordReviewApplyResDto
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApply
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApplyStatus
import com.example.jhouse_server.domain.record_review_apply.repository.RecordReviewApplyRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.bucket.RateLimitService
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.exception.ErrorCode.INVALID_VALUE_EXCEPTION
import com.example.jhouse_server.global.exception.ErrorCode.UNAUTHORIZED_EXCEPTION
import com.example.jhouse_server.global.util.RedisUtil
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@Service
@Transactional(readOnly = true)
class RecordServiceImpl(
    private val userRepository: UserRepository,
    private val recordReviewRepository: RecordReviewRepository,
    private val recordReviewApplyRepository: RecordReviewApplyRepository,
    private val recordCommentRepository: RecordCommentRepository,
    private val recordRepository: RecordRepository,
    private val odoriRepository: OdoriRepository,
    private val retrospectionRepository: RetrospectionRepository,
    private val technologyRepository: TechnologyRepository,
    private val redisUtil: RedisUtil,
    private val rateLimitService: RateLimitService
): RecordService {

    private val HITS_EXPIRE: Long = 60 //60 * 60 * 24

    @Transactional
    override fun saveRecord(recordReqDto: RecordReqDto, user: User): Long {
        val record = Record(recordReqDto.title, recordReqDto.content, Part.getPart(recordReqDto.part)!!, user)
        val subRecord = matchDType(record, recordReqDto.category, recordReqDto.dType)
        val id = recordRepository.save(subRecord).id
        applyForReview(subRecord, user)

        return id
    }

    @Transactional
    override fun updateRecord(recordUpdateDto: RecordUpdateDto, user: User, recordId: Long): Long {
        val record = recordRepository.findByIdOrThrow(recordId)
        validateUser(record, user)
        record.updateRecord(recordUpdateDto)
        return record.id
    }

    @Transactional
    override fun deleteRecord(user: User, recordId: Long) {
        val record = recordRepository.findByIdOrThrow(recordId)
        validateUser(record, user)
        recordRepository.delete(record)
    }

    override fun getHotRecords(): RecordHotResDto {
        val weekAgo = LocalDateTime.now().minusWeeks(7)
        val hotRecords = recordRepository.findHotRecords(weekAgo)
        return RecordHotResDto(hotRecords)
    }

    override fun getRecords(condition: RecordPageCondition, pageable: Pageable): RecordPageResDto {
        return when (condition.dType) {
            "all" -> {
                val records = recordRepository.findRecords(condition, pageable)
                RecordPageResDto(records)
            }
            "odori" -> {
                val records = odoriRepository.findOdoris(condition, pageable)
                RecordPageResDto(records)
            }
            "retro" -> {
                val records = retrospectionRepository.findRetrospections(condition, pageable)
                RecordPageResDto(records)
            }
            "tech" -> {
                val records = technologyRepository.findTechnologies(condition, pageable)
                RecordPageResDto(records)
            }
            else -> throw ApplicationException(INVALID_VALUE_EXCEPTION)
        }
    }

    @Transactional
    override fun getRecord(recordId: Long, request: HttpServletRequest, pageable: Pageable): RecordResDto {
        val record = recordRepository.findByIdOrThrow(recordId)
        val comments = recordCommentRepository.findRecordComments(record, pageable)
        updateHits(request, record)

        return when (recordRepository.findDType(recordId)) {
            "O" -> {
                val odori = record as Odori
                RecordResDto(odori.id, odori.title!!, odori.content!!, odori.hits, odori.part!!.value,
                    "odori", odori.category.value, odori.user!!.nickName, odori.createdAt, comments)
            }
            "R" -> {
                val retro = record as Retrospection
                RecordResDto(retro.id, retro.title!!, retro.content!!, retro.hits, retro.part!!.value,
                    "retro", retro.category.value, retro.user!!.nickName, retro.createdAt, comments)
            }
            "T" -> {
                val tech = record as Technology
                RecordResDto(tech.id, tech.title!!, tech.content!!, tech.hits, tech.part!!.value,
                    "tech", tech.category.value, tech.user!!.nickName, tech.createdAt, comments)
            }
            else -> throw ApplicationException(INVALID_VALUE_EXCEPTION)
        }
    }

    override fun getRecordWithReview(recordId: Long): RecordWithReviewResDto {
        val record = recordRepository.findByIdWithUser(recordId)
            .orElseThrow { ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION) }
        val recordReviewApplies = recordReviewApplyRepository.findByRecordWithUser(recordId)
        val recordReviews = recordReviewRepository.findByRecordWithUser(recordId)
        val recordReviewApplyDtoList = recordReviewApplies
            .map { RecordReviewApplyResDto(it.status.value, it.reviewer.nickName) }
        val recordReviewDtoList = recordReviews
            .map { RecordReviewResDto(it.id, it.content, it.status.value, it.reviewer.nickName, it.createdAt) }
        return RecordWithReviewResDto(record.id, record.title!!, record.content!!, record.hits,
            record.part!!.value, record.user!!.nickName, record.createdAt, recordReviewDtoList, recordReviewApplyDtoList)
    }

    override fun getRevieweeRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): RecordPageResDto {
        val records = recordRepository.findRevieweeRecords(condition, user, pageable)
        return RecordPageResDto(records)
    }

    override fun getReviewerRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): RecordPageResDto {
        val records = recordRepository.findReviewerRecords(condition, user, pageable)
        return RecordPageResDto(records)
    }

    /**
     * 하나의 리뷰가 들어온 이상 WAIT 상태에서 APPROVE, REJECT 상태로 바뀌어야 한다.
     * 자신을 제외한 모든 신청자의 상태가 APPROVE 이면 APPROVE 상태로 변경
     */
    override fun updateRecordStatus(record: Record) {
        val recordReviewApplies = record.applies
        if(recordReviewApplies.filter { it.status == RecordReviewApplyStatus.APPROVE }.size == recordReviewApplies.size - 1) {
            record.updateRecordStatus(RecordStatus.APPROVE)
        } else if(recordReviewApplies.any { it.status == RecordReviewApplyStatus.REJECT }) {
            record.updateRecordStatus(RecordStatus.REJECT)
        }
    }

    private fun updateHits(request: HttpServletRequest, record: Record) {
        val ip = rateLimitService.getClientIp(request)
        val key = ip + "_" + record.id.toString()
        if (redisUtil.getValues(key) == null) {
            redisUtil.setValuesExpired(key, record.id.toString(), HITS_EXPIRE)
            record.updateHits()
        }
    }

    private fun matchDType(record: Record, category: String, dType: String): Record {
        return when (dType) {
            "odori" -> Odori(OdoriCategory.getCategory(category)!!, record)
            "retro" -> Retrospection(RetrospectionCategory.getCategory(category)!!, record)
            "tech" -> Technology(TechnologyCategory.getCategory(category)!!, record)
            else -> throw ApplicationException(INVALID_VALUE_EXCEPTION)
        }
    }

    private fun validateUser(record: Record, user: User) {
        if(user != record.user) {
            throw ApplicationException(UNAUTHORIZED_EXCEPTION)
        }
    }

    private fun applyForReview(record: Record, user: User) {
        recordReviewApplyRepository.save(RecordReviewApply(RecordReviewApplyStatus.MINE, record, user))
        val reviewers = userRepository.findAllByAdminType(user.id, user.adminType!!)
        reviewers.forEach {
            val recordReviewApply = RecordReviewApply(RecordReviewApplyStatus.WAIT, record, it)
            recordReviewApplyRepository.save(recordReviewApply)
        }
    }
}