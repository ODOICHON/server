package com.example.jhouse_server.admin.house.dto


data class AdminHouseSearch(
    val filter: HouseSearchFilter?,
    val keyword: String?
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

data class RejectForm(
    val houseId : Long?,
    val reason : String?
)