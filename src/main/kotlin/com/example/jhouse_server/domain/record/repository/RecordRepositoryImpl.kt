package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.record.entity.QRecord.record
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.repository.common.RecordCommonMethod
import com.example.jhouse_server.domain.record_review_apply.entity.QRecordReviewApply.recordReviewApply
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApplyStatus
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import java.time.LocalDateTime

class RecordRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val recordCommonMethod: RecordCommonMethod
): RecordRepositoryCustom {

    override fun findHotRecords(weekAgo: LocalDateTime): List<RecordHotThumbnailResDto> {
        return jpaQueryFactory
            .select(QRecordHotThumbnailResDto(record.id, record.title))
            .from(record)
            .where(record.createdAt.goe(weekAgo), record.status.eq(RecordStatus.APPROVE))
            .orderBy(record.hits.desc(), record.createdAt.desc())
            .limit(3)
            .fetch()
    }

    override fun findRestHotRecords(weekAgo: LocalDateTime, limit: Long): List<RecordHotThumbnailResDto> {
        return jpaQueryFactory
            .select(QRecordHotThumbnailResDto(record.id, record.title))
            .from(record)
            .where(record.createdAt.lt(weekAgo), record.status.eq(RecordStatus.APPROVE))
            .orderBy(record.createdAt.desc(), record.hits.desc())
            .limit(limit)
            .fetch()
    }

    override fun findRecords(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), user.nickName, record.createdAt, record.part.stringValue().toLowerCase()))
            .from(record)
            .leftJoin(record.user, user)
            .where(
                record.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.recordPartEq(condition.part),
                recordCommonMethod.categoryEq(condition.type, condition.category)
            )
            .orderBy(record.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), user.nickName, record.createdAt, record.part.stringValue().toLowerCase()))
            .from(record)
            .leftJoin(record.user, user)
            .where(
                record.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.recordPartEq(condition.part),
                recordCommonMethod.categoryEq(condition.type, condition.category)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }

    override fun findRevieweeRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), QUser.user.nickName, record.createdAt, record.part.stringValue().toLowerCase()))
            .from(record)
            .leftJoin(record.user, QUser.user)
            .where(
                record.user.eq(user),
                recordStatusEq(condition.status)
            )
            .orderBy(record.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content, QUser.user.nickName, record.createdAt, record.part.stringValue().toLowerCase()))
            .from(record)
            .leftJoin(record.user, QUser.user)
            .where(
                record.user.eq(user),
                recordStatusEq(condition.status)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }

    override fun findReviewerRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), QUser.user.nickName, record.createdAt, record.part.stringValue().toLowerCase()))
            .from(recordReviewApply)
            .leftJoin(recordReviewApply.record, record)
            .leftJoin(recordReviewApply.reviewer, QUser.user)
            .where(
                recordReviewApply.reviewer.eq(user),
                recordReviewApplyStatusEq(condition.status)
            )
            .orderBy(recordReviewApply.record.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), QUser.user.nickName, record.createdAt, record.part.stringValue().toLowerCase()))
            .from(recordReviewApply)
            .leftJoin(recordReviewApply.record, record)
            .leftJoin(recordReviewApply.reviewer, QUser.user)
            .where(
                recordReviewApply.reviewer.eq(user),
                recordReviewApplyStatusEq(condition.status)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }

    private fun recordStatusEq(status: String?): BooleanExpression? {
        if(!StringUtils.hasText(status)) return null
        return record.status.eq(RecordStatus.getStatus(status!!))
    }

    private fun recordReviewApplyStatusEq(status: String?): BooleanExpression? {
        if(!StringUtils.hasText(status)) return null
        return recordReviewApply.status.eq(RecordReviewApplyStatus.getStatus(status!!))
    }
}