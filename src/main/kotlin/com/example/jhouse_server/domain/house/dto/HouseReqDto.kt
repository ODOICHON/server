package com.example.jhouse_server.domain.house.dto

import com.example.jhouse_server.domain.house.entity.*
import com.example.jhouse_server.domain.user.entity.UserType
import org.hibernate.validator.constraints.Length
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

/**
 * ================================================================================================
 * 빈집 매물 게시글 작성/수정 DTO
 * ================================================================================================
 * */
data class HouseReqDto(
    var houseType: HouseType,
    var rentalType: RentalType? = null,
    var city: String?,
    var detail: String?,
    var zipCode: String?,
    var size: String?,
    var purpose: String?,
    var floorNum: Int,
    var contact: String?,
    var createdDate: String?,
    var price: Int,
    var monthlyPrice: Double,
    var agentName: String?, // 공인중개사인 경우
    var title: String?, // 게시글 제목
    var code: String?, // 게시글 내용
    var imageUrls: List<String>?, // 이미지 주소
    val tmpYn: Boolean, // 임시저장 false -> save , true -> tmpSave
    var recommendedTag: List<String>?, // 추천 태그
    var agentDetail: String? // 공인중개사인 경우
)

/**
 * ================================================================================================
 * 빈집 매물 게시글 목록 조회 시, 요청 Param
 * ================================================================================================
 * */
data class HouseListDto(
    var houseType: String?, // 빈집 매물 타입
    val rentalType: String?, // 빈집 매물 유형
    val city: String?, // 매물 위치
    val recommendedTag: List<RecommendedTag>?, // 추천 태그
    val search: String?, // 검색어 ( 제목과 닉네임 )
    val dealState: String? //
) : Serializable

/**
 * ================================================================================================
 * 마이페이지) 빈집 매물 게시글 목록 조회 DTO -- 공인중개사
 * ================================================================================================
 * */
data class HouseAgentListDto(
    val search: String?,
    val dealState: String?,
    val isCompleted: Boolean?, //거래 기능 개발 후 판매상태 조건 추가
    val houseType: String?,
)

/**
 * ================================================================================================
 * 마이페이지) 빈집 매물 게시글 목록 조회 DTO -- 일반 사용자
 * ================================================================================================
 * */
data class MyHouseResDto(
    val houseId: Long,
    var houseType: HouseType,
    val rentalType: RentalType,
    val city: String,
    val title: String,
    val imageUrl: String,
    val dealState: String,
    val dealStateName: String
)

/**
 * ================================================================================================
 * 빈집 매물 게시글 리스트 조회 시, 응답 DTO
 * ================================================================================================
 * */
class HouseResDto() : Serializable {
    var houseId: Long = 0 // 게시글 아이디
    lateinit var houseType: HouseType // 매물 타입
    lateinit var rentalType: RentalType // 매물 유형
    lateinit var city: String // 매물 위치
    var price: Int? = 0 // 매물 가격 ( 월세의 경우, 보증금 )
    var monthlyPrice: Double? = 0.0 // 매물 월세
    lateinit var nickName: String // 매물 작성자 닉네임
    lateinit var createdAt: Date // 게시글 작성일자 ( yyyy-MM-dd ) -> 클라이언트 측에서 파싱
    var isCompleted: Boolean = false // 매물 거래 여부
    var imageUrl: String? = null // 썸네일
    lateinit var title: String // 게시글 제목
    lateinit var recommendedTag: List<RecommendedTag> // 추천 태그
    lateinit var recommendedTagName: List<String> // 추천 태그명

    constructor(
        houseId: Long,
        houseType: HouseType,
        rentalType: RentalType,
        city: String,
        price: Int,
        monthlyPrice: Double,
        nickName: String,
        createdAt: Date,
        isCompleted: Boolean,
        imageUrl: String?,
        title: String,
        recommendedTag: List<RecommendedTag>,
        recommendedTagName: List<String>
    ) : this() {
        this.houseId = houseId
        this.houseType = houseType
        this.rentalType = rentalType
        this.city = city
        this.price = price
        this.monthlyPrice = monthlyPrice
        this.nickName = nickName
        this.createdAt = createdAt
        this.isCompleted = isCompleted
        this.imageUrl = imageUrl
        this.title = title
        this.recommendedTag = recommendedTag
        this.recommendedTagName = recommendedTagName
    }
}

/**
 * ================================================================================================
 * 빈집 매물 게시글 단일 조회 시, 응답 DTO
 * ================================================================================================
 * */
data class HouseResOneDto(
    val houseId: Long,
    var houseType: HouseType,
    val rentalType: RentalType,
    val city: String,
    val detail: String?,
    val zipCode: String,
    val size: String,
    val purpose: String?,
    val floorNum: Int,
    val contact: String,
    val createdDate: String?, // 준공연도
    val price: Int,
    val monthlyPrice: Double,
    val agentName: String, // 공인중개사명
    val title: String,
    val code: String,
    val imageUrls: List<String>,
    val nickName: String, // 게시글 작성자
    val userType: UserType, // 게시글 작성자의 타입 ( 일반회원, 공인중개사 )
    val createdAt: Date,
    val isCompleted: Boolean, // 거래 완료 여부
    val isScraped: Boolean, // 게시글 스크랩 여부
    val recommendedTag: List<RecommendedTag>, // 게시글 추천 태그
    val recommendedTagName: List<String>, // 게시글 추천 태그명
    val agentDetail: String? // 공인중개사 상세정보
)

/**
 * ================================================================================================
 * 빈집 매물 게시글 신고 요청 DTO
 * ================================================================================================
 * */
data class ReportReqDto(
    @field:NotNull(message = "신고사유는 필수값입니다.")
    val reportType: String, // 신고분류
    @field:Length(min = 1, max = 100)
    val reportReason: String // 신고사유
)

/**
 * ================================================================================================
 * 빈집 매물 게시글 거래 상태 변경 요청 DTO
 * ================================================================================================
 * */
data class DealReqDto(
    @field:NotNull(message = "만족도 점수는 필수값입니다.")
    val score: Int,
    val review: String?,
    val nickName: String?,
    val age: String?,
    @field:NotNull(message = "구매자 연락처 정보는 필수값입니다.")
    val contact: String,
    @field:Pattern(
        regexp = "\"\"\"^\\d{4}-\\d{2}-\\d{2}\$\"\"\"",
        message = "팔린날짜는 필수값입니다. ( yyyy-MM-dd )"
    )
    val dealDate: String,
)

/**
 * ================================================================================================
 * PUBLIC FUNCTION -- DTO <> ENTITY
 * ================================================================================================
 * */

fun getTagByNameFromHouseTags(houseTag: List<RecommendedTag>): List<RecommendedTag> {
    return houseTag.stream().map { RecommendedTag.getTagByName(it.name) }.toList()
}

fun toListDto(house: House): HouseResDto {
    val recommendedTag: List<RecommendedTag> =
        getTagByNameFromHouseTags(house.houseTag.stream().map { it.recommendedTag }.toList())
    val recommendedTagName: List<String> =
        house.houseTag.stream().map { RecommendedTag.getValueByTagName(it.recommendedTag.name) }
            .toList()
    val imageUrl = if (house.imageUrls.isEmpty()) null else house.imageUrls[0]
    return HouseResDto(
        house.id,
        house.houseType,
        house.rentalType,
        house.address.city,
        house.price,
        house.monthlyPrice,
        house.user.nickName,
        Timestamp.valueOf(house.createdAt),
        house.dealState == DealState.COMPLETED,
        imageUrl,
        house.title,
        recommendedTag,
        recommendedTagName
    )
}

fun toDto(house: House, isScraped: Boolean): HouseResOneDto {
    val recommendedTag: List<RecommendedTag> =
        getTagByNameFromHouseTags(house.houseTag.stream().map { it.recommendedTag }.toList())
    val recommendedTagName: List<String> =
        house.houseTag.stream().map { RecommendedTag.getValueByTagName(it.recommendedTag.name) }
            .toList()
    return HouseResOneDto(
        house.id,
        house.houseType,
        house.rentalType,
        house.address.city,
        house.address.detail,
        house.address.zipCode,
        house.size,
        house.purpose,
        house.floorNum,
        house.contact,
        house.createdDate,
        house.price,
        house.monthlyPrice,
        house.agentName,
        house.title,
        house.code,
        house.imageUrls,
        house.user.nickName,
        house.user.userType,
        Timestamp.valueOf(house.createdAt),
        house.dealState == DealState.COMPLETED,
        isScraped,
        recommendedTag,
        recommendedTagName,
        house.agentDetail
    )
}

fun toMyHouseDto(house: House): MyHouseResDto {
    return MyHouseResDto(
        house.id,
        house.houseType,
        house.rentalType,
        house.address.city,
        house.title,
        house.imageUrls[0],
        house.dealState.name,
        house.dealState.value
    )
}