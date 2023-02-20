package com.example.jhouse_server.domain.user

import com.example.jhouse_server.domain.user.entity.User
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Pattern

data class UserSignUpReqDto(
        @field:Pattern(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$", message = "이메일 형식에 맞지 않습니다.")
        val email: String,      //계정@도메인.최상위도메인
        @field:Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@!%*#?&])[A-Za-z\\d@!%*#?&]{8,}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String,   //최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자
        @JsonProperty("nick_name") val nickName: String,
        @JsonProperty("phone_num") val phoneNum: String
)

data class UserSignInReqDto(
        val email: String,
        val password: String
)

data class CheckSmsReqDto(
        @JsonProperty("phone_num") val phoneNum: String,
        val code: String
)

data class UserResDto(
        @JsonProperty("user_id") val userId: Long,
        @JsonProperty("nick_name") val nickName: String,
        @JsonProperty("phone_num") val phoneNum: String
)

fun toDto(user: User) : UserResDto {
    return UserResDto(user.id!!, user.nickName, user.phoneNum)
}
