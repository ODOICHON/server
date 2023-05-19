package com.example.jhouse_server.domain.record_comment.repository

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_comment.dto.QRecordCommentResDto
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentResDto
import com.example.jhouse_server.domain.record_comment.entity.QRecordComment.recordComment
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class RecordCommentRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): RecordCommentRepositoryCustom {

    override fun findNextRef(record: Record): Int {
        return jpaQueryFactory
            .select(recordComment.ref.count())
            .from(recordComment)
            .where(recordComment.record.eq((record)))
            .groupBy(recordComment.ref)
            .fetch().size
    }

    override fun findRecordComments(record: Record, pageable: Pageable): Page<RecordCommentResDto> {
        val content = jpaQueryFactory
            .select(QRecordCommentResDto(recordComment.id, recordComment.level, recordComment.content, user.nickName, recordComment.createdAt))
            .from(recordComment)
            .leftJoin(recordComment.user, user)
            .where(recordComment.record.eq(record))
            .orderBy(
                recordComment.ref.asc(),
                recordComment.step.asc()
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordCommentResDto(recordComment.id, recordComment.level, recordComment.content, user.nickName, recordComment.createdAt))
            .from(recordComment)
            .leftJoin(recordComment.user, user)
            .where(recordComment.record.eq(record))

        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetch().size.toLong() }
    }
}
