package com.example.jhouse_server.domain

data class UserReqDto(
        val nickName: String,
        val phoneNum: String
)

data class UserResDto(
        val userId: Long,
        val nickName: String,
        val phoneNum: String
)

fun toDto(user: User) : UserResDto {
    return UserResDto(user.id!!, user.nickName, user.phoneNum)
}
