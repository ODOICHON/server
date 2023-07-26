package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.admin.report.dto.ReportSearch
import com.example.jhouse_server.domain.house.entity.QHouse.house
import com.example.jhouse_server.domain.house.entity.QReport.report
import com.example.jhouse_server.domain.house.repository.dto.*
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class ReportRepositoryImpl(
    val jpaQueryFactory: JPAQueryFactory
): ReportRepositoryCustom{


    override fun getReports(reportSearch: ReportSearch, pageable: Pageable): Page<ReportDto> {
        val result = jpaQueryFactory
            .select(
                QReportQueryDto(
                    user,
                    report.count()
                )
            )
            .from(report)
            .join(report.house, house)
            .join(house.user, user)
            .where(searchReportFilter(reportSearch), house.useYn.eq(true))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .groupBy(user)
            .orderBy(user.suspension.desc())
            .fetch()

        val countQuery = jpaQueryFactory
            .select(
                QReportQueryDto(
                    user,
                    report.count()
                )
            )
            .from(report)
            .join(report.house, house)
            .join(house.user, user)
            .where(searchReportFilter(reportSearch), house.useYn.eq(true))
            .groupBy(user, report)

        return PageableExecutionUtils.getPage(getReportDto(result), pageable) {countQuery.fetch().size.toLong()}
    }

    override fun getReportDetail(id: Long): List<ReportDetailQueryDto> {

        val agent = QUser("agent")
        val reporter = QUser("reporter")

        return jpaQueryFactory
            .select(QReportDetailQueryDto(report.createdAt, house.id, reporter.nickName, report.reportType))
            .from(report)
            .join(report.house, house)
            .join(report.reporter, reporter)
            .join(house.user, agent)
            .where(agent.id.eq(id), house.useYn.eq(true), house.reported.eq(true))
            .orderBy(report.createdAt.desc())
            .fetch()
    }

    private fun searchReportFilter(reportSearch: ReportSearch): BooleanExpression?{
        return when(reportSearch.filter){
            null -> null
            else -> user.nickName.contains(reportSearch.keyword)
        }
    }

    private fun getReportDto(result: List<ReportQueryDto>): List<ReportDto> {
        val reportDtos = mutableListOf<ReportDto>()
        result.forEach {
            val agent = it.user as Agent
            reportDtos.add(ReportDto(agent.id, agent.nickName, agent.agentName, agent.phoneNum, it.reportCount, agent.suspension))
        }
        return reportDtos
    }


}