package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.record.entity.QRecord.record
import com.example.jhouse_server.domain.record.repository.common.RecordCommonMethod
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

class RecordRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val recordCommonMethod: RecordCommonMethod
): RecordRepositoryCustom {

    override fun findHotRecords(weekAgo: LocalDateTime): List<RecordHotThumbnailResDto> {
        return jpaQueryFactory
            .select(QRecordHotThumbnailResDto(record.id, record.title))
            .from(record)
            .where(record.createdAt.goe(weekAgo))
            .orderBy(record.hits.desc(), record.createdAt.desc())
            .limit(3)
            .fetch()
    }

    override fun findRecordsByUser(user: User, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), QUser.user.nickName, record.createdAt, record.part))
            .from(record)
            .leftJoin(record.user, QUser.user)
            .where(record.user.eq(user))
            .orderBy(record.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content, QUser.user.nickName, record.createdAt, record.part))
            .from(record)
            .leftJoin(record.user, QUser.user)
            .where(record.user.eq(user))

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }

    override fun findRecords(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), user.nickName, record.createdAt, record.part))
            .from(record)
            .leftJoin(record.user, user)
            .where(
                recordCommonMethod.recordPartEq(condition.dType, condition.part),
                recordCommonMethod.categoryEq(condition.dType, condition.category)
            )
            .orderBy(record.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(record.id, record.title, record.content.substring(0, 50), user.nickName, record.createdAt, record.part))
            .from(record)
            .leftJoin(record.user, user)
            .where(
                recordCommonMethod.recordPartEq(condition.dType, condition.part),
                recordCommonMethod.categoryEq(condition.dType, condition.category)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }
}