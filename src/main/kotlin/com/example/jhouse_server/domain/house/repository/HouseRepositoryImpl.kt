package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.admin.house.dto.AdminHouseDto
import com.example.jhouse_server.admin.house.dto.AdminHouseSearch
import com.example.jhouse_server.admin.house.dto.HouseSearchFilter
import com.example.jhouse_server.domain.board.repository.dto.CountQueryDto
import com.example.jhouse_server.domain.board.repository.dto.CustomMyPageImpl
import com.example.jhouse_server.domain.house.dto.HouseAgentListDto
import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.dto.MyHouseResDto
import com.example.jhouse_server.domain.house.dto.toMyHouseDto
import com.example.jhouse_server.domain.house.entity.*
import com.example.jhouse_server.domain.house.entity.QHouse.house
import com.example.jhouse_server.domain.scrap.entity.QScrap.scrap
import com.example.jhouse_server.domain.user.entity.QUser
import com.example.jhouse_server.domain.user.entity.QUser.user
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils


class HouseRepositoryImpl(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
    private var jpaQueryFactory: JPAQueryFactory
): HouseRepositoryCustom {
    /**
     * =============================================================================================
     *  빈집 게시글 전체 조회
     *  --rentalType, useYn, tmpYn, reported, applied
     *  --title, useYn, tmpYn, reported, applied
     * =============================================================================================
     * */
    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .innerJoin(house.user)
            .leftJoin(house.houseTag)
            .where(
                searchWithKeyword(houseListDto.search), // 키워드 검색어
                searchWithRentalType(houseListDto.rentalType), // 매물 타입 필터링
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장 X
                house.reported.eq(false), // 신고 X
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                filterWithCity(houseListDto.city), // 매물 위치 필터링
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
            .innerJoin(house.user)
            .leftJoin(house.houseTag)
            .where(
                searchWithKeyword(houseListDto.search), // 키워드 검색어
                searchWithRentalType(houseListDto.rentalType), // 매물 타입 필터링
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장 X
                house.reported.eq(false), // 신고 X
                house.applied.eq(HouseReviewStatus.APPROVE), // 게시글 미신청 ( 관리자 승인 혹은 공인중개사 게시글 )
                filterWithCity(houseListDto.city), // 매물 위치 필터링
                filterWithRecommendedTags(houseListDto.recommendedTag), // 게시글 추천 태그 필터링
                filterWithDealState(houseListDto.dealState), // 판매 여부
            )
            .fetch().size.toLong()

        return PageableExecutionUtils.getPage(result, pageable) { countQuery }
    }

    /**
     * ============================================================================================
     * 자신이 작성한 임시저장된 게시글 목록 조회
     * ============================================================================================
     * */
    override fun getTmpSaveHouseAll(user: User, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .innerJoin(house.user)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(true), // 임시저장 X
                house.reported.eq(false), // 신고 X
                house.user.eq(user),
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()

        val countQuery : Long = jpaQueryFactory
            .selectFrom(house)
            .innerJoin(house.user)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(true), // 임시저장 X
                house.reported.eq(false), // 신고 X
                house.user.eq(user),
            )
            .fetch().size.toLong()
        return PageableExecutionUtils.getPage(result, pageable) { countQuery }
    }

    /**
     * ============================================================================================
     * 자신이 스크랩한 게시글 목록 조회
     * ============================================================================================
     */
    override fun getScrapHouseAll(user: User, filter: String?, pageable: Pageable): Page<House> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, QUser.user)
            .join(house.scrap, scrap)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                scrap.subscriber.eq(user),
                filterWithDealState(filter) // 판매중 필터링
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, QUser.user)
            .join(house.scrap, scrap)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.reported.eq(false), // 신고 X
                scrap.subscriber.eq(user),
                filterWithDealState(filter)
            )
        return PageableExecutionUtils.getPage(result, pageable) { countQuery.fetch().size.toLong() }
    }

    /**
     * ============================================================================================
     * 마이페이지 빈집거래 목록 조회 (공인중개사)
     *
     * 거래 기능 개발 후, 판매상태 동적 조건 추가 필요
     * 일반 사용자도 경우도 Deal 테이블과 연관해서 필요
     * ============================================================================================
     */
    override fun getAgentHouseAll(user: User, houseAgentListDto: HouseAgentListDto, pageable: Pageable): Page<MyHouseResDto> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                searchTitleWithKeyword(houseAgentListDto.search), // 키워드 검색어
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val cntAllQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
            ).fetch().size.toLong()
        val cntApplyQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                house.dealState.eq(DealState.APPLYING)
            ).fetch().size.toLong()
        val cntOngoingQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                house.dealState.eq(DealState.ONGOING)
            ).fetch().size.toLong()
        val cntCompletedQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                house.dealState.eq(DealState.COMPLETED)
            ).fetch().size.toLong()
        val resultDtoList = result.map { toMyHouseDto(it) }
        val housePage =  PageableExecutionUtils.getPage(resultDtoList, pageable) { cntAllQuery }
        val countQueryDto = CountQueryDto(cntAllQuery, cntApplyQuery, cntOngoingQuery, cntCompletedQuery)
        return CustomMyPageImpl(housePage.content, housePage.number, housePage.size, housePage.totalElements, countQueryDto)
    }

    /**
     * ============================================================================================
     * 빈집거래 승인대기 게시글 목록 조회
     * ============================================================================================
     */
    override fun getApplyHouseListWithPaging(
        adminHouseSearch: AdminHouseSearch,
        pageable: Pageable
    ): Page<AdminHouseDto> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, user)
            .where(
                searchFilter(adminHouseSearch),
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false),
                house.applied.eq(HouseReviewStatus.APPLY)
            )
            .orderBy(house.id.asc())
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .groupBy(house)
            .fetch()
        val countQuery = jpaQueryFactory
            .selectFrom(house)
            .join(house.user, user)
            .where(
                searchFilter(adminHouseSearch),
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false),
                house.applied.eq(HouseReviewStatus.APPLY)
            )
            .groupBy(house)
        return PageableExecutionUtils.getPage(getHouseDto(result), pageable) {countQuery.fetch().size.toLong()}
    }

    /**
     * ============================================================================================
     * 마이페이지 빈집거래 목록 조회 (일반사용자)
     * 거래 상태에 따른 집계 쿼리 추가
     * ============================================================================================
     */
    override fun getMyHouseAll(user: User, keyword: String?, pageable: Pageable): Page<MyHouseResDto> {
        val result = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                searchWithKeyword(keyword), // 닉네임 & 제목
                searchWithKeywordFullText(keyword),
            )
            .limit(pageable.pageSize.toLong())
            .offset(pageable.offset)
            .fetch()
        val cntAllQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
            ).fetch().size.toLong()
        val cntApplyQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                house.dealState.eq(DealState.APPLYING)
            ).fetch().size.toLong()
        val cntOngoingQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                house.dealState.eq(DealState.ONGOING)
            ).fetch().size.toLong()
        val cntCompletedQuery = jpaQueryFactory
            .selectFrom(house)
            .leftJoin(house.deal, QDeal.deal)
            .where(
                house.useYn.eq(true), // 삭제 X
                house.tmpYn.eq(false), // 임시저장
                house.reported.eq(false), // 신고 X
                house.user.eq(user), // 본인인지
                house.dealState.eq(DealState.COMPLETED)
            ).fetch().size.toLong()
        val resultDtoList = result.map { toMyHouseDto(it) }
        val housePage =  PageableExecutionUtils.getPage(resultDtoList, pageable) { cntAllQuery }
        val countQueryDto = CountQueryDto(cntAllQuery, cntApplyQuery, cntOngoingQuery, cntCompletedQuery)
        return CustomMyPageImpl(housePage.content, housePage.number, housePage.size, housePage.totalElements, countQueryDto)
    }
    /**
     * ============================================================================================
     * PRIVATE FUNCTION
     * ============================================================================================
     * */

    private fun searchTitleWithKeyword(keyword: String?) : BooleanExpression? {
        return if(keyword.isNullOrEmpty()) null else house.title.contains(keyword)
    }

    /**
     * ============================================================================================
     * 승인요청된 빈집 거래 게시글 목록 조회
     * => 데이터 가공 처리 로직
     * ============================================================================================
     * */
    private fun getHouseDto(result: List<House>): List<AdminHouseDto> {
        val list = mutableListOf<AdminHouseDto>()
        result.forEach{
            val applied = it.applied == HouseReviewStatus.APPROVE
            list.add(AdminHouseDto(it.id, it.user.nickName, it.title, applied))
        }
        return list
    }

    /**
     * ============================================================================================
     * 관리자 페이지 - 빈집 거래 승인
     * 게시글 제목, 내용, 작성자 검색
     * ============================================================================================
     * */
    private fun searchFilter(adminHouseSearch: AdminHouseSearch): BooleanExpression? {
        return when(adminHouseSearch.filter) {
            HouseSearchFilter.TITLE -> house.title.contains(adminHouseSearch.keyword)
            HouseSearchFilter.CONTENT -> house.content.contains(adminHouseSearch.keyword)
            HouseSearchFilter.WRITER -> user.nickName.contains(adminHouseSearch.keyword)
            else -> null
        }
    }

    /**
     * ============================================================================================
     * 검색어 필터링 함수
     * 게시글 제목과 게시글 작성자 닉네임
     * ============================================================================================
     * */
    private fun searchWithKeyword(keyword: String?): BooleanExpression? {
        return if(keyword == null) null else house.user.nickName.contains(keyword).or(house.title.contains(keyword)).or(searchWithKeywordFullText(keyword))
    }
    /**
     * ============================================================================================
     * 검색어 필터링 함수
     * 게시글 내용 -- FULL TEXT SEARCH
     * ============================================================================================
     * */
    private fun searchWithKeywordFullText(keyword: String?) : BooleanExpression? {
        val contentBoolean = Expressions.numberTemplate(
            Integer::class.java, "function('match',{0},{1})", house.content,
            "+$keyword*"
        )

        return contentBoolean.gt(0)
    }

    /**
     * ============================================================================================
     * 지역 필터링 함수
     * 수도권 -> 서울, 인천, 경기
     * 그외 -> city
     * ============================================================================================
     * */
    private fun filterWithCity(city: String?): BooleanExpression? {
        return if(city == null) null
        else {
            if(city == "수도권") house.address.city.contains("서울").or(house.address.city.contains("인천")).or(house.address.city.contains("경기"))
            else house.address.city.contains(city)
        }
    }

    /**
     * ============================================================================================
     * 추천 태그 필터링 함수
     * [] -> isEmpty()
     * 그외 -> Enum name
     * ============================================================================================
     * */
    private fun filterWithRecommendedTags(recommendedTag : List<RecommendedTag>?): BooleanExpression? {
        return if(recommendedTag.isNullOrEmpty()) null else {
            house.houseTag.any().recommendedTag.`in`(
                JPAExpressions.select(QHouseTag.houseTag.recommendedTag)
                    .from(QHouseTag.houseTag)
                    .where(QHouseTag.houseTag.recommendedTag.`in`(recommendedTag)))
        }
    }
    /**
     * ============================================================================================
     * 빈집 매물 거래 상태 필터링
     * ============================================================================================
     * */
    private fun filterWithDealState(dealState: String?): BooleanExpression? {
        return if(dealState.isNullOrBlank()) null else house.dealState.eq(DealState.valueOf(dealState))
    }
    /**
     * ============================================================================================
     * 빈집 매물 필터링
     * ============================================================================================
     * */
    private fun searchWithRentalType(rentalType: String?): BooleanExpression? {
        return if(rentalType.isNullOrBlank()) null else house.rentalType.eq(RentalType.valueOf(rentalType))
    }
}