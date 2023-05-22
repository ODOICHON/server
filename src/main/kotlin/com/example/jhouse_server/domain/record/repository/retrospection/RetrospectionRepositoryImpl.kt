package com.example.jhouse_server.domain.record.repository.retrospection

import com.example.jhouse_server.domain.record.dto.QRecordThumbnailResDto
import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordThumbnailResDto
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.entity.retrospection.QRetrospection.retrospection
import com.example.jhouse_server.domain.record.repository.common.RecordCommonMethod
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class RetrospectionRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val recordCommonMethod: RecordCommonMethod
): RetrospectionRepositoryCustom {

    override fun findRetrospections(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(retrospection.id, retrospection.title, retrospection.content.substring(0, 50), user.nickName, retrospection.createdAt, retrospection.part))
            .from(retrospection)
            .leftJoin(retrospection.user, user)
            .where(
                retrospection.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.retroPartEq(condition.part),
                recordCommonMethod.categoryEq(condition.type, condition.category)
            )
            .orderBy(retrospection.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(retrospection.id, retrospection.title, retrospection.content.substring(0, 50), user.nickName, retrospection.createdAt, retrospection.part))
            .from(retrospection)
            .leftJoin(retrospection.user, user)
            .where(
                retrospection.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.retroPartEq(condition.part),
                recordCommonMethod.categoryEq(condition.type, condition.category)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }
}