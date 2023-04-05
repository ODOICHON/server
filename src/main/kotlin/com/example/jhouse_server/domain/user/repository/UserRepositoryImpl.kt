package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.QUserJoinPath.*
import com.example.jhouse_server.domain.user.repository.dto.AdminUserAnalysisAgeResult
import com.example.jhouse_server.domain.user.repository.dto.AdminUserAnalysisJoinPathResult
import com.example.jhouse_server.domain.user.repository.dto.QAdminUserAnalysisAgeResult
import com.example.jhouse_server.domain.user.repository.dto.QAdminUserAnalysisJoinPathResult
import com.querydsl.jpa.impl.JPAQueryFactory
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
                .fetchCount()

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
                .fetchCount()

        return getOrderJoinPathRateResult(queryResult, total)
    }

    private fun getOrderAgeRateResult(queryResult : List<AdminUserAnalysisAgeResult>, total : Long) : List<Double> {
        val resultList = mutableListOf<Double>()
        queryResult.forEach { r -> resultList.add(getRate(total, r.count)) }
        // switch
        val resultTwenty = resultList[0]
        val resultTen = resultList[1]
        resultList[0] = resultTen
        resultList[1] = resultTwenty

        return resultList
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