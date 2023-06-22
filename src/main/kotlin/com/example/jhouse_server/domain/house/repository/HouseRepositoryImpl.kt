package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.QHouse.house
import com.example.jhouse_server.domain.house.entity.RentalType
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class HouseRepositoryImpl(
    private var jpaQueryFactory: JPAQueryFactory
): HouseRepositoryCustom {
    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable) : Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, user).fetchJoin()
            .where(
                house.useYn.eq(true), // 삭제 X
                house.houseType.eq(RentalType.valueOf(houseListDto.rentalType)), // 매물 타입 필터링
                filterWithCity(houseListDto.city), // 매물 위치 필터링
                searchWithKeyword(houseListDto.search), // 키워드 검색어
                house.reported.eq(false), // 신고 X
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(house)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.houseType.eq(RentalType.valueOf(houseListDto.rentalType)), // 매물 타입 필터링
                filterWithCity(houseListDto.city), // 매물 위치 필터링
                searchWithKeyword(houseListDto.search), // 키워드 검색어
                house.reported.eq(false), // 신고 X
            )
        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetch().size.toLong()}
    }

    /**
     * 검색어 필터링 함수
     * 게시글 제목과 게시글 작성자 닉네임
     * */
    private fun searchWithKeyword(keyword: String?): BooleanExpression? {
        return if(keyword == null) null else house.user.nickName.contains(keyword).or(house.title.contains(keyword))
    }

    /**
     * 지역 필터링 함수
     * 수도권 -> 서울, 인천, 경기
     * 그외 -> city
     * */
    private fun filterWithCity(city: String?): BooleanExpression? {
        return if(city == null) null
        else {
            if(city == "수도권") house.address.city.contains("서울")
                .or(house.address.city.contains("인천"))
                .or(house.address.city.contains("경기"))
            else house.address.city.contains(city)
        }
    }
}