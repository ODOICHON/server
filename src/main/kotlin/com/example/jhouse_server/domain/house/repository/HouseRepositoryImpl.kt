package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.dto.HouseAgentListDto
import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.entity.*
import com.example.jhouse_server.domain.house.entity.QHouse.house
import com.example.jhouse_server.domain.house.entity.QHouseTag.houseTag
import com.example.jhouse_server.domain.house.entity.RentalType
import com.example.jhouse_server.domain.scrap.entity.QScrap.scrap
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import kotlin.streams.toList


class HouseRepositoryImpl(
    private var jpaQueryFactory: JPAQueryFactory
): HouseRepositoryCustom {
    /**
     * TODO N+1 문제 해결을 위한 DTO transform()으로 튜닝
     * */
    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .innerJoin(house.user)
            .leftJoin(house.houseTag)
            .where(
                house.useYn.eq(true), // 삭제 X
                searchWithRentalType(houseListDto.rentalType), // 매물 타입 필터링
                filterWithCity(houseListDto.city), // 매물 위치 필터링
                searchWithKeyword(houseListDto.search), // 키워드 검색어
                house.reported.eq(false), // 신고 X
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                house.tmpYn.eq(false), // 임시저장 X
                filterWithRecommendedTags(houseListDto.recommendedTag), // 게시글 추천 태그 필터링
                filterWithDealState(houseListDto.dealState), // 판매 여부
            )
            .groupBy(house.id)
            .orderBy(house.updatedAt.desc()) // 최신순
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        val countQuery : Long = jpaQueryFactory
            .selectFrom(house)
            .where(
                house.useYn.eq(true), // 삭제 X
                searchWithRentalType(houseListDto.rentalType),
                filterWithCity(houseListDto.city), // 매물 위치 필터링
                searchWithKeyword(houseListDto.search), // 키워드 검색어
                house.reported.eq(false), // 신고 X
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                house.tmpYn.eq(false), // 임시저장 X
                filterWithRecommendedTags(houseListDto.recommendedTag),
                filterWithDealState(houseListDto.dealState), //
            )
            .fetch().size.toLong()

        return PageableExecutionUtils.getPage(result, pageable) { countQuery }
    }

    private fun filterWithDealState(dealState: String?): BooleanExpression? {
        return if(dealState.isNullOrBlank()) null else house.dealState.eq(DealState.valueOf(dealState))
    }

    private fun searchWithRentalType(rentalType: String?): BooleanExpression? {
        return if(rentalType.isNullOrBlank()) null else house.rentalType.eq(RentalType.valueOf(rentalType))
    }

    /**
     * 자신이 작성한 임시저장된 게시글 목록 조회
     * */
    override fun getTmpSaveHouseAll(user: User, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .innerJoin(house.user)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                house.tmpYn.eq(true), // 임시저장 X
                house.user.eq(user)
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        val countQuery : Long = jpaQueryFactory
            .selectFrom(house)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                house.tmpYn.eq(true), // 임시저장 X
                house.user.eq(user)
            )
            .fetch().size.toLong()
        return PageableExecutionUtils.getPage(result, pageable) { countQuery }
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

    /**
     * 추천 태그 필터링 함수
     * [] -> isEmpty()
     * 그외 -> Enum name
     * */
    private fun filterWithRecommendedTags(recommendedTag : List<RecommendedTag>?): BooleanExpression? {
        return if(recommendedTag.isNullOrEmpty()) null else {
            house.houseTag.any().recommendedTag.`in`(
                JPAExpressions.select(QHouseTag.houseTag.recommendedTag)
                    .from(QHouseTag.houseTag)
                    .where(QHouseTag.houseTag.recommendedTag.`in`(recommendedTag)))
        }
    }

}