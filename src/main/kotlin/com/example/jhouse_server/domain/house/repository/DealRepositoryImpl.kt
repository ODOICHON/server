package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.admin.house.dto.AdminDealDto
import com.example.jhouse_server.admin.house.dto.AdminHouseSearch
import com.example.jhouse_server.admin.house.dto.HouseSearchFilter
import com.example.jhouse_server.domain.house.entity.Deal
import com.example.jhouse_server.domain.house.entity.HouseReviewStatus
import com.example.jhouse_server.domain.house.entity.QDeal
import com.example.jhouse_server.domain.house.entity.QDeal.deal
import com.example.jhouse_server.domain.house.entity.QHouse
import com.example.jhouse_server.domain.house.entity.QHouse.house
import com.example.jhouse_server.domain.user.entity.Age
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import java.time.LocalDate

class DealRepositoryImpl(
    private var jpaQueryFactory: JPAQueryFactory
) : DealRepositoryCustom {


    override fun getReviewHouseListWithPaging(
        adminHouseSearch: AdminHouseSearch,
        pageable: Pageable
    ): Page<AdminDealDto> {
        val result = jpaQueryFactory
            .selectFrom(QDeal.deal)
            .leftJoin(QDeal.deal.house, house)
            .leftJoin(QDeal.deal.buyer, user)
            .where(
                searchFilter(adminHouseSearch),
                ageFilter(adminHouseSearch.age),
                scoreFilter(adminHouseSearch.score),
            )
            .orderBy(deal.id.asc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .groupBy(deal)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(QDeal.deal)
            .leftJoin(QDeal.deal.house, house)
            .leftJoin(QDeal.deal.buyer, user)
            .where(
                searchFilter(adminHouseSearch),
                ageFilter(adminHouseSearch.age),
                scoreFilter(adminHouseSearch.score),
            )
            .orderBy(deal.id.asc())
            .groupBy(deal)
        return PageableExecutionUtils.getPage(getDealDto(result), pageable) {countQuery.fetch().size.toLong()}
    }

    private fun scoreFilter(score: Int?): BooleanExpression? {
        return if(score != null) deal.score.eq(score) else null
    }

    private fun ageFilter(age: String?): BooleanExpression? {
        return if(!age.isNullOrEmpty()) deal.buyer.age.eq(Age.valueOf(age)) else null
    }

    private fun getDealDto(result: List<Deal>): MutableList<AdminDealDto> {
        val list = mutableListOf<AdminDealDto>()
        result.forEach {
            list.add(AdminDealDto(it.id, it.buyer.nickName, it.house.title, it.score, it.dealDate, it.buyer.phoneNum, it.buyer.age.value, it.review, it.house.id))
        }
        return list
    }

    /**
     * 관리자 페이지 - 빈집 거래 승인
     * 게시글 제목, 내용, 작성자 검색
     * */
    private fun searchFilter(adminHouseSearch: AdminHouseSearch): BooleanExpression? {
        return when(adminHouseSearch.filter) {
            HouseSearchFilter.TITLE -> QHouse.house.title.contains(adminHouseSearch.keyword)
            HouseSearchFilter.CONTENT -> QHouse.house.content.contains(adminHouseSearch.keyword)
            HouseSearchFilter.WRITER -> QUser.user.nickName.contains(adminHouseSearch.keyword)
            else -> null
        }
    }
}