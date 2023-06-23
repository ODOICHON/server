package com.example.jhouse_server.domain.house.dto

import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.RentalType
import com.example.jhouse_server.global.aop.CodeValid
import java.sql.Timestamp
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class HouseReqDto(
    @field:NotNull(message = "매매 타입은 필수값입니다.")
    val rentalType: RentalType? = null,
    @field:NotNull(message = "주소는 필수값입니다. ( 예: 서울/인천 ~ )")
    val city: String?,
    @field:NotNull(message = "우편번호는 필수값입니다.")
    val zipCode: String?,
    @field:NotNull(message = "집 크기는 필수값입니다. ( m^2 단위로 산정해서 작성할 것 )")
    val size: String?,
    @field:NotNull(message = "매물 목적/용도는 필수값입니다.")
    val purpose: String?,
    var floorNum: Int,
    @field:NotNull(message = "연락처는 필수값입니다.")
    var contact: String?,
    var createdDate: String,
    @field:NotNull(message = "매물 가격은 필수값입니다.")
    var price: Int?,
    var monthlyPrice: Double,
    val agentName : String,
    val title: String,
    @CodeValid
    val code: String,
    val imageUrls: List<String>,
    val tmpYn : Boolean, // false -> save , true -> tmpSave
)
data class HouseListDto(
    val rentalType: String,
    val city: String?,
    val search: String?
)
data class HouseResDto(
    val houseId: Long,
    val rentalType: RentalType,
    val city: String,
    val price: Int,
    val monthlyPrice: Double,
    val nickName: String,
    val createdAt: Date,
    val isCompleted: Boolean,
    val imageUrl : String // 썸네일
)
data class HouseResOneDto(
    val houseId: Long,
    val rentalType: RentalType,
    val city: String,
    val zipcode: String,
    val size: String,
    val purpose: String,
    val floorNum: Int,
    val contact: String,
    val createdDate: String, // 준공연도
    val price: Int,
    val monthlyPrice: Double,
    val agentName: String, // 공인중개사명
    val title: String,
    val code: String,
    val imageUrls: List<String>,
    val nickName: String, // 게시글 작성자
    val createdAt: Date,
    val isCompleted: Boolean, // 거래 완료 여부
    val isScraped : Boolean, // 게시글 스크랩 여부
)

data class ReportReqDto(
    val reportReason: String
)

fun toDto(house: House, isScraped: Boolean) : HouseResOneDto {
    return HouseResOneDto(house.id, house.houseType, house.address.city,
        house.address.zipcode, house.size, house.purpose, house.floorNum, house.contact,
        house.createdDate, house.price, house.monthlyPrice,
        house.agentName, house.title, house.code, house.imageUrls, house.user.nickName,
        Timestamp.valueOf(house.createdAt), false, isScraped)
}

fun toListDto(house: House) : HouseResDto {
    return HouseResDto(house.id, house.houseType!!, house.address.city, house.price, house.monthlyPrice, house.user.nickName, Timestamp.valueOf(house.createdAt), false, house.imageUrls[0] )
}