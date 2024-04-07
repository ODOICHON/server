package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisAgeResponse
import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse
import com.example.jhouse_server.admin.user.dto.AdminAgentSearch
import com.example.jhouse_server.admin.user.dto.AdminUserList
import com.example.jhouse_server.admin.user.dto.AdminUserWithdrawalSearch
import com.example.jhouse_server.domain.user.entity.Age
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.QUserJoinPath.userJoinPath
import com.example.jhouse_server.domain.user.entity.QUserTerm.userTerm
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType.NONE
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.WAIT
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.entity.agent.QAgent.agent
import com.example.jhouse_server.domain.user.repository.dto.AdminUserAnalysisAgeResult
import com.example.jhouse_server.domain.user.repository.dto.AdminUserAnalysisJoinPathResult
import com.example.jhouse_server.domain.user.repository.dto.QAdminUserAnalysisAgeResult
import com.example.jhouse_server.domain.user.repository.dto.QAdminUserAnalysisJoinPathResult
import com.querydsl.core.group.GroupBy
import com.querydsl.core.group.GroupBy.groupBy
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.util.stream.Collectors
import kotlin.math.round

class UserRepositoryImpl(
        var jpaQueryFactory: JPAQueryFactory
) : UserRepositoryCustom {
    /**
     * ============================================================================================
     * 사용자 연령대 조회
     * ============================================================================================
     * */
    override fun getAnalysisAgeResult() : List<AnalysisAgeResponse> {
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
    /**
     * ============================================================================================
     * 사용자 회원 가입 경로 조회
     * ============================================================================================
     * */
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
    /**
     * ============================================================================================
     * 공인중개사 회원 가입 승인 대기 목록 조회
     * ============================================================================================
     * */
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
    /**
     * ============================================================================================
     * 공인중개사 회원 탈퇴 요청 목록 조회
     * ============================================================================================
     * */
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
    /**
     * ============================================================================================
     * 사용자 회원 탈퇴 요청 목록 조회
     * ============================================================================================
     * */
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

    override fun getUserWithSearchForm(
        adminUserSearch: AdminUserWithdrawalSearch,
        pageable: Pageable
    ): Page<AdminUserList> {
        val result = jpaQueryFactory.selectFrom(user)
            .leftJoin(userJoinPath).on(user.id.eq(userJoinPath.user.id))
            .leftJoin(userTerm).on(user.id.eq(userTerm.user.id))
            .where(searchUserFilter(adminUserSearch), user.authority.eq(Authority.USER))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .transform(groupBy(user.id).list( Projections.constructor(
                AdminUserList::class.java,
                user.id,
                user.nickName,
                user.email,
                user.userType,
                user.phoneNum,
                user.createdAt,
                user.age.stringValue(),
                GroupBy.set(
                    userJoinPath.joinPath.stringValue()
                ),
                GroupBy.set(
                    userTerm.term.stringValue()
                )
            )))

        val map = mutableMapOf<Long, AdminUserList>()
        // result의 아이템 중 age, joinPath, term enum의 value로 치환하고 싶어.
        result.forEach{
            val userId = it.id
            val age = Age.valueOf(it.age)
            val transformedJoinPath = it.getJoinPathValues().distinct().toSet() ?: it.joinPath
            val transformedTerm = it.getTermValues().distinct().toSet() ?: it.term
            // map에 userId가 이미 있다면, 해당 userId의 AdminUserList를 가져와서 joinPath, term을 추가하고 다시 map에 넣어줘.
            if(map.containsKey(userId)){
                val adminUserList = map[userId]
                adminUserList?.joinPath?.plusElement(transformedJoinPath)
                adminUserList?.term?.plusElement(transformedTerm)
                return@forEach
            }
            map[userId] = AdminUserList(
                it.id,
                it.nickName,
                it.email,
                it.userType,
                it.phoneNum,
                it.createdAt,
                age.value,
                transformedJoinPath,
                transformedTerm
            )
        }
        // map의 value만 뽑아서 list로 만들어서 반환해줘.
        val transformedResult = map.values.toList()

        val countQuery = jpaQueryFactory
            .selectFrom(user)
            .leftJoin(userJoinPath).on(user.id.eq(userJoinPath.user.id))
            .leftJoin(userTerm).on(user.id.eq(userTerm.user.id))
            .where(searchUserFilter(adminUserSearch))
            .fetch().size.toLong()
        return PageImpl(transformedResult, pageable, countQuery)
    }
    /**
     * ============================================================================================
     * PRIVATE FUNCTION
     * ============================================================================================
     * */

    /**
     * ============================================================================================
     * 연령대 비율 결과 응답 DTO로 반환
     * @param queryResult
     * @param total
     * @return List<AnalysisAgeResponse>
     * ============================================================================================
     * */
    private fun getOrderAgeRateResult(queryResult : List<AdminUserAnalysisAgeResult>, total : Long) : List<AnalysisAgeResponse> {
        return queryResult
            .stream()
            .map { r -> AnalysisAgeResponse(r.age.value, getRate(total, r.count), r.count.toInt()) }
            .collect(Collectors.toList())
    }
    /**
     * ============================================================================================
     * 공인중개사 검색 조건
     * @param adminAgentSearch
     * @return BooleanExpression
     * ============================================================================================
     * */
    private fun searchAgentFilter(adminAgentSearch: AdminAgentSearch): BooleanExpression? {
        return when(adminAgentSearch.keyword) {
            null -> null
            else -> agent.companyName.contains(adminAgentSearch.keyword)
        }
    }
    /**
     * ============================================================================================
     * 사용자 검색 ( 닉네임 )
     * @param adminUserWithdrawalSearch
     * @return BooleanExpression
     * ============================================================================================
     * */
    private fun searchUserFilter(adminUserWithdrawalSearch: AdminUserWithdrawalSearch): BooleanExpression? {
        return when(adminUserWithdrawalSearch.filter){
            null -> null
            else -> user.nickName.contains(adminUserWithdrawalSearch.keyword)
        }
    }
    /**
     * ============================================================================================
     * 사용자 가입 경로 비율 결과 응답 DTO로 변환
     * @param queryResult
     * @param total
     * @return List<AnalysisJoinPathResponse>
     * ============================================================================================
     * */
    private fun getOrderJoinPathRateResult(queryResult : List<AdminUserAnalysisJoinPathResult>, total : Long) : List<AnalysisJoinPathResponse> {
        return queryResult
                .stream()
                .map { r -> AnalysisJoinPathResponse(r.joinPath.value, getRate(total, r.count), r.count.toInt()) }
                .collect(Collectors.toList())
    }
    /**
     * ============================================================================================
     * 비율 계산 함수
     * ============================================================================================
     * */
    private fun getRate(total : Long, count : Long) : Double {
        return round((count / total.toDouble() * 100)*100 ) / 100
    }

}