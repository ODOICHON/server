package com.example.jhouse_server.domain.record.repository.odori

import com.example.jhouse_server.domain.record.dto.QRecordThumbnailResDto
import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordThumbnailResDto
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.entity.odori.QOdori.odori
import com.example.jhouse_server.domain.record.repository.common.RecordCommonMethod
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

class OdoriRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory,
    private val recordCommonMethod: RecordCommonMethod
): OdoriRepositoryCustom {

    override fun findOdoris(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto> {
        val content = jpaQueryFactory
            .select(QRecordThumbnailResDto(odori.id, odori.title, odori.content.substring(0, 50), user.nickName, odori.createdAt, odori.part))
            .from(odori)
            .leftJoin(odori.user, user)
            .where(
                odori.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.odoriPartEq(condition.dType, condition.part),
                recordCommonMethod.categoryEq(condition.dType, condition.category)
            )
            .orderBy(odori.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(QRecordThumbnailResDto(odori.id, odori.title, odori.content.substring(0, 50), user.nickName, odori.createdAt, odori.part))
            .from(odori)
            .leftJoin(odori.user, user)
            .where(
                odori.status.eq(RecordStatus.APPROVE),
                recordCommonMethod.odoriPartEq(condition.dType, condition.part),
                recordCommonMethod.categoryEq(condition.dType, condition.category)
            )

        return recordCommonMethod.createPage(content, pageable, countQuery)
    }
}