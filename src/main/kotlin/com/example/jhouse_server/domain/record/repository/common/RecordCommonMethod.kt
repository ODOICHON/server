package com.example.jhouse_server.domain.record.repository.common

import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record.entity.QRecord.record
import com.example.jhouse_server.domain.record.entity.RecordType
import com.example.jhouse_server.domain.record.entity.odori.OdoriCategory
import com.example.jhouse_server.domain.record.entity.odori.QOdori.odori
import com.example.jhouse_server.domain.record.entity.retrospection.QRetrospection.retrospection
import com.example.jhouse_server.domain.record.entity.retrospection.RetrospectionCategory
import com.example.jhouse_server.domain.record.entity.technology.QTechnology.technology
import com.example.jhouse_server.domain.record.entity.technology.TechnologyCategory
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component

@Component
class RecordCommonMethod {

    fun <T> createPage(content: List<T>, pageable: Pageable, countQuery: JPAQuery<T>): Page<T> {
        return PageableExecutionUtils.getPage(content, pageable) { countQuery.fetch().size.toLong() }
    }

    fun recordPartEq(part: String): BooleanExpression? {
        return when (Part.getPart(part)) {
            Part.ALL -> null
            Part.WEB -> record.part.eq(Part.WEB)
            Part.SERVER -> record.part.eq(Part.SERVER)
            Part.INFRA -> record.part.eq(Part.INFRA)
        }
    }

    fun odoriPartEq(part: String): BooleanExpression? {
        return when (Part.getPart(part)) {
            Part.ALL -> null
            Part.WEB -> odori.part.eq(Part.WEB)
            Part.SERVER -> odori.part.eq(Part.SERVER)
            Part.INFRA -> odori.part.eq(Part.INFRA)
        }
    }

    fun retroPartEq(part: String): BooleanExpression? {
        return when (Part.getPart(part)) {
            Part.ALL -> null
            Part.WEB -> retrospection.part.eq(Part.WEB)
            Part.SERVER -> retrospection.part.eq(Part.SERVER)
            Part.INFRA -> retrospection.part.eq(Part.INFRA)
        }
    }

    fun techPartEq(part: String): BooleanExpression? {
        return when (Part.getPart(part)) {
            Part.ALL -> null
            Part.WEB -> technology.part.eq(Part.WEB)
            Part.SERVER -> technology.part.eq(Part.SERVER)
            Part.INFRA -> technology.part.eq(Part.INFRA)
        }
    }

    fun categoryEq(type: String, category: String): BooleanExpression? {
        return when (RecordType.getType(type)) {
            RecordType.ALL -> null
            RecordType.ODORI -> odori.category.eq(OdoriCategory.getCategoryByEnum(category))
            RecordType.RETRO -> retrospection.category.eq(RetrospectionCategory.getCategoryByEnum(category))
            RecordType.TECH -> technology.category.eq(TechnologyCategory.getCategoryByEnum(category))
        }
    }
}