package com.example.jhouse_server.domain.record.repository.common

import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record.entity.QRecord
import com.example.jhouse_server.domain.record.entity.QRecord.record
import com.example.jhouse_server.domain.record.entity.odori.OdoriCategory
import com.example.jhouse_server.domain.record.entity.odori.QOdori
import com.example.jhouse_server.domain.record.entity.odori.QOdori.odori
import com.example.jhouse_server.domain.record.entity.retrospection.QRetrospection
import com.example.jhouse_server.domain.record.entity.retrospection.QRetrospection.retrospection
import com.example.jhouse_server.domain.record.entity.retrospection.RetrospectionCategory
import com.example.jhouse_server.domain.record.entity.technology.QTechnology
import com.example.jhouse_server.domain.record.entity.technology.QTechnology.technology
import com.example.jhouse_server.domain.record.entity.technology.TechnologyCategory
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
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

    fun recordPartEq(dType: String, part: String): BooleanExpression? {
        return when (part) {
            "all" -> null
            "web" -> record.part.eq(Part.WEB)
            "server" -> record.part.eq(Part.SERVER)
            "infra" -> record.part.eq(Part.INFRA)
            else -> throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
    }

    fun odoriPartEq(dType: String, part: String): BooleanExpression? {
        return when (part) {
            "all" -> null
            "web" -> odori.part.eq(Part.WEB)
            "server" -> odori.part.eq(Part.SERVER)
            "infra" -> odori.part.eq(Part.INFRA)
            else -> throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
    }

    fun retroPartEq(dType: String, part: String): BooleanExpression? {
        return when (part) {
            "all" -> null
            "web" -> retrospection.part.eq(Part.WEB)
            "server" -> retrospection.part.eq(Part.SERVER)
            "infra" -> retrospection.part.eq(Part.INFRA)
            else -> throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
    }

    fun techPartEq(dType: String, part: String): BooleanExpression? {
        return when (part) {
            "all" -> null
            "web" -> technology.part.eq(Part.WEB)
            "server" -> technology.part.eq(Part.SERVER)
            "infra" -> technology.part.eq(Part.INFRA)
            else -> throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
    }

    fun categoryEq(dType: String, category: String): BooleanExpression? {
        return when (dType) {
            "all" -> null
            "odori" -> odori.category.eq(OdoriCategory.getCategoryByEnum(category))
            "retro" -> retrospection.category.eq(RetrospectionCategory.getCategoryByEnum(category))
            "tech" -> technology.category.eq(TechnologyCategory.getCategoryByEnum(category))
            else -> throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
    }
}