package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.dto.HouseAgentListDto
import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.HouseReviewStatus
import com.example.jhouse_server.domain.house.entity.QHouse.house
import com.example.jhouse_server.domain.house.entity.RentalType
import com.example.jhouse_server.domain.scrap.entity.QScrap.scrap
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.User
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
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                house.tmpYn.eq(false), // 임시저장 X
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
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                house.tmpYn.eq(false), // 임시저장 X
            )
        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetch().size.toLong()}
    }

    /**
     * 자신이 작성한 임시저장된 게시글 목록 조회
     * */
    override fun getTmpSaveHouseAll(user: User, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, QUser.user).fetchJoin()
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                house.tmpYn.eq(true), // 임시저장 X
                house.user.eq(user)
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(house)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                house.tmpYn.eq(true), // 임시저장 X
                house.user.eq(user)
            )
        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetch().size.toLong()}
    }

    /**
     * 자신이 스크랩한 게시글 목록 조회
     */
    override fun getScrapHouseAll(user: User, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, QUser.user).fetchJoin()
            .join(house.scrap, scrap)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                scrap.subscriber.eq(user)
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(house)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                scrap.subscriber.eq(user)
            )
        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetch().size.toLong() }
    }

    /**
     * 마이페이지 빈집거래 목록 조회 (공인중개사)
     *
     * 거래 기능 개발 후, 판매상태 동적 조건 추가 필요
     * 일반 사용자도 경우도 Deal 테이블과 연관해서 필요
     */
    override fun getAgentHouseAll(user: User, houseAgentListDto: HouseAgentListDto, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, QUser.user).fetchJoin()
            .where(
                house.useYn.eq(true), // 삭제 X
                searchWithKeywordAgent(houseAgentListDto.search), // 키워드 검색어
                house.reported.eq(false), // 신고 X
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                house.tmpYn.eq(false), // 임시저장 X
                house.user.eq(user)
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(house)
            .where(
                house.useYn.eq(true), // 삭제 X
                searchWithKeywordAgent(houseAgentListDto.search), // 키워드 검색어
                house.reported.eq(false), // 신고 X
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                house.tmpYn.eq(false), // 임시저장 X
                house.user.eq(user)
            )
        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetch().size.toLong() }
    }

    /**
     * 게시글 제목 필터링
     */
    private fun searchWithKeywordAgent(keyword: String?): BooleanExpression? {
        return if (keyword == null) null
        else house.title.contains(keyword)
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
            if(city == "수도권") house.address.city.contains("서울").or(house.address.city.contains("인천")).or(house.address.city.contains("경기"))
            else house.address.city.contains(city)
        }
    }
}