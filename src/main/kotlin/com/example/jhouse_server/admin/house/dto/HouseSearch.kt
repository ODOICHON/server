package com.example.jhouse_server.admin.house.dto

import java.time.LocalDate


data class AdminHouseSearch(
    val filter: HouseSearchFilter?,
    val keyword: String?,
    val score: Int?,
    val age: String?,
)

enum class HouseSearchFilter (val value: String) {
    TITLE("게시물 제목"),
    CONTENT("게시물 내용"),
    WRITER("게시물 작성자"),
    ;
}

data class AdminHouseApplyList(
    val applyHouseList: List<Long>?
)

data class AdminHouseDto(
    val id : Long,
    val nickName: String,
    val title: String,
    val applied : Boolean
)

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
data class RejectForm(
    val houseId : Long?,
    val reason : String?
)