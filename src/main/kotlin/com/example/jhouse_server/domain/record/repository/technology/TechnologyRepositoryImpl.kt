package com.example.jhouse_server.domain.record.repository.technology

import com.example.jhouse_server.domain.record.dto.QRecordThumbnailResDto
import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordThumbnailResDto
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.entity.technology.QTechnology.technology
import com.example.jhouse_server.domain.record.repository.common.RecordCommonMethod
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class TechnologyRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val recordCommonMethod: RecordCommonMethod
): TechnologyRepositoryCustom {

    override fun findTechnologies(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(technology.id, technology.title, technology.content.substring(0, 50), user.nickName, technology.createdAt, technology.part))
            .from(technology)
            .leftJoin(technology.user, user)
            .where(
                technology.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.techPartEq(condition.dType, condition.part),
                recordCommonMethod.categoryEq(condition.dType, condition.category)
            )
            .orderBy(technology.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(technology.id, technology.title, technology.content.substring(0, 50), user.nickName, technology.createdAt, technology.part))
            .from(technology)
            .leftJoin(technology.user, user)
            .where(
                technology.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.techPartEq(condition.dType, condition.part),
                recordCommonMethod.categoryEq(condition.dType, condition.category)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }
}