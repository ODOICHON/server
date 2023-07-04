package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse
import com.example.jhouse_server.admin.user.dto.join.AdminAgentSearch
import com.example.jhouse_server.admin.user.dto.withdrawal.AdminUserWithdrawalSearch
import com.example.jhouse_server.admin.user.dto.withdrawal.UserSearchFilter
import com.example.jhouse_server.domain.user.entity.*
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.QUserJoinPath.*
import com.example.jhouse_server.domain.user.entity.UserType.*
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.entity.agent.QAgent.*
import com.example.jhouse_server.domain.user.repository.dto.AdminUserAnalysisAgeResult
import com.example.jhouse_server.domain.user.repository.dto.AdminUserAnalysisJoinPathResult
import com.example.jhouse_server.domain.user.repository.dto.QAdminUserAnalysisAgeResult
import com.example.jhouse_server.domain.user.repository.dto.QAdminUserAnalysisJoinPathResult
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.util.stream.Collectors
import kotlin.math.round

class UserRepositoryImpl(
        var jpaQueryFactory: JPAQueryFactory
) : UserRepositoryCustom {

    override fun getAnalysisAgeResult() : List<Double> {
        val queryResult = jpaQueryFactory
                .select(QAdminUserAnalysisAgeResult(user.age, user.age.count()))
                .from(user)
                .where(user.authority.eq(Authority.USER))
                .groupBy(user.age)
                .fetch()
        val total = jpaQueryFactory
                .selectFrom(user)
                .where(user.authority.eq(Authority.USER))
            .fetch().size.toLong()

        return getOrderAgeRateResult(queryResult, total)
    }

    override fun getAnalysisJoinPathResults() : List<AnalysisJoinPathResponse> {
        val queryResult = jpaQueryFactory
                .select(QAdminUserAnalysisJoinPathResult(userJoinPath.joinPath, userJoinPath.joinPath.count()))
                .from(userJoinPath)
                .join(userJoinPath.user, user)
                .where(user.authority.eq(Authority.USER))
                .groupBy(userJoinPath.joinPath)
                .orderBy(userJoinPath.joinPath.count().desc())
                .fetch()

        val total = jpaQueryFactory
                .selectFrom(userJoinPath)
                .join(userJoinPath.user, user)
                .where(user.authority.eq(Authority.USER))
            .fetch().size.toLong()

        return getOrderJoinPathRateResult(queryResult, total)
    }

    private fun getOrderAgeRateResult(queryResult : List<AdminUserAnalysisAgeResult>, total : Long) : List<Double> {

        val rateList = queryResult.map { r -> getRate(total, r.count) }
        val ageList = queryResult.map { r -> r.age }

        val result = mutableListOf<Double>()
        for (i in Age.values()){
            if (i in ageList){
                for (j in queryResult.indices){
                    if (queryResult[j].age == i){
                        result.add(rateList[j])
                    }
                }
            } else {
                result.add(0.00)
            }
        }

        return result
    }

    override fun getWaitingAgentResult(adminAgentSearch: AdminAgentSearch, pageable: Pageable): Page<Agent> {
        val result = jpaQueryFactory
                .selectFrom(agent)
                .where(searchAgentFilter(adminAgentSearch), agent.status.eq(AgentStatus.WAIT))
                .orderBy(agent.createdAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val countQuery = jpaQueryFactory
                .selectFrom(agent)
                .where(searchAgentFilter(adminAgentSearch), agent.status.eq(AgentStatus.WAIT))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}
    }

    override fun getAgentWithdrawalReqResult(adminAgentSearch: AdminAgentSearch, pageable: Pageable): Page<Agent> {
        val result = jpaQueryFactory
                .selectFrom(agent)
                .where(searchAgentFilter(adminAgentSearch), agent.withdrawalStatus.eq(WAIT))
                .orderBy(agent.updatedAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val countQuery = jpaQueryFactory
                .selectFrom(agent)
                .where(searchAgentFilter(adminAgentSearch), agent.withdrawalStatus.eq(WAIT))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}

    }

    override fun getUserWithdrawalReqResult(adminUserWithdrawalSearch: AdminUserWithdrawalSearch, pageable: Pageable): Page<User> {
        val result = jpaQueryFactory
                .selectFrom(user)
                .where(searchUserFilter(adminUserWithdrawalSearch), user.withdrawalStatus.eq(WAIT), user.userType.eq(NONE))
                .orderBy(user.updatedAt.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()

        val countQuery = jpaQueryFactory
                .selectFrom(user)
                .where(searchUserFilter(adminUserWithdrawalSearch), user.withdrawalStatus.eq(WAIT), user.userType.eq(NONE))

        return PageableExecutionUtils.getPage(result, pageable) {countQuery.fetch().size.toLong()}
    }

    private fun searchAgentFilter(adminAgentSearch: AdminAgentSearch): BooleanExpression? {
        return when(adminAgentSearch.keyword) {
            null -> null
            else -> agent.companyName.contains(adminAgentSearch.keyword)
        }
    }

    private fun searchUserFilter(adminUserWithdrawalSearch: AdminUserWithdrawalSearch): BooleanExpression? {
        return when(adminUserWithdrawalSearch.filter){
            null -> null
            else -> user.nickName.contains(adminUserWithdrawalSearch.keyword)
        }
    }


    private fun getOrderJoinPathRateResult(queryResult : List<AdminUserAnalysisJoinPathResult>, total : Long) : List<AnalysisJoinPathResponse> {
        return queryResult
                .stream()
                .map { r -> AnalysisJoinPathResponse(r.joinPath.value, getRate(total, r.count)) }
                .collect(Collectors.toList())
    }

    private fun getRate(total : Long, count : Long) : Double {
        return round((count / total.toDouble() * 100)*100 ) / 100
    }

}