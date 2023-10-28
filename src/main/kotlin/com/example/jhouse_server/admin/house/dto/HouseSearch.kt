package com.example.jhouse_server.admin.house.dto

import java.time.LocalDate

/**
 * =============================================================================================
 * AdminHouseSearch                -- 빈집 게시글 검색 요청 DTO
 * filter                          -- 검색 필터링
 * score                           -- 만족도 점수
 * age                             -- 구매자 연령대
 * =============================================================================================
 */
data class AdminHouseSearch(
    val filter: HouseSearchFilter?,
    val keyword: String?,
    val score: Int?,
    val age: String?,
)

/**
 * =============================================================================================
 * AdminHouseApplyList                -- 빈집 게시글 일괄 승인 요청 DTO
 * applyHouseList                     -- 빈집 게시글 ID
 * =============================================================================================
 */
data class AdminHouseApplyList(
    val applyHouseList: List<Long>?
)
/**
 * =============================================================================================
 * AdminHouseDto                   -- 빈집 게시글 응답 DTO
 * id                              -- 게시글 ID
 * nickName                        -- 작성자 이름
 * title                           -- 제목
 * applied                         -- 승인여부
 * =============================================================================================
 */
data class AdminHouseDto(
    val id : Long,
    val nickName: String,
    val title: String,
    val applied : Boolean
)
/**
 * =============================================================================================
 * AdminDealDto                    -- 빈집 거래 후기 게시글 DTO
 * id                              -- 거래 ID
 * nickName                        -- 게시글 작성자 이름
 * title                           -- 제목
 * score                           -- 만족도 점수
 * dealDate                        -- 판매상태
 * contact                         -- 연락처
 * age                             -- 연령대
 * review                          -- 후기
 * houseId                         -- 빈집 게시글 ID
 * =============================================================================================
 */
data class AdminDealDto(
    val id : Long, // dealId
    val nickName: String, // buyer
    val title: String, // house
    val score: Int,
    val dealDate : LocalDate,
    val contact : String,
    val age: String,
    val review : String?,
    val houseId : Long
)
/**
 * =============================================================================================
 * RejectForm                      -- 빈집 게시글 반려 요청 DTO
 * houseId                         -- 게시글 ID
 * reason                          -- 반려 사유
 * =============================================================================================
 */
data class RejectForm(
    val houseId : Long?,
    val reason : String?
)
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */

enum class HouseSearchFilter (val value: String) {
    TITLE("게시물 제목"),
    CONTENT("게시물 내용"),
    WRITER("게시물 작성자"),
    ;
}