package com.example.jhouse_server.domain.user

import com.example.jhouse_server.domain.user.entity.Age
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserSignUpReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "이메일 형식에 맞지 않습니다.")
        val email: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[A-Za-z0-9가-힣]{1,20}\$", message = "닉네임 형식에 맞지 않습니다.")
        @JsonProperty("nick_name") val nickName: String,
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}", message = "전화번호 형식에 맞지 않습니다.")
        @JsonProperty("phone_num") val phoneNum: String,
        @NotNull val age: String,
        @NotNull @JsonProperty("join_paths") val joinPaths: MutableList<String>
)

data class UserSignInReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "이메일 형식에 맞지 않습니다.")
        val email: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String
)

data class CheckSmsReqDto(
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}", message = "전화번호 형식에 맞지 않습니다.")
        @JsonProperty("phone_num") val phoneNum: String,
        @field:Pattern(regexp = "^[0-9]{4}", message = "인증번호 형식에 맞지 않습니다.")
        val code: String
)

data class EmailReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "이메일 형식에 맞지 않습니다.")
        val email: String
)

data class PhoneNumReqDto(
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}", message = "전화번호 형식에 맞지 않습니다.")
        @JsonProperty("phone_num") val phoneNum: String
)

data class NickNameReqDto(
        @field:Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[A-Za-z0-9가-힣]{1,20}\$", message = "닉네임 형식에 맞지 않습니다.")
        @JsonProperty("nick_name") val nickName: String
)

data class PasswordReqDto(
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String
)

data class UserResDto(
        @JsonProperty("id") val id: Long,
        val email: String,
        @JsonProperty("nick_name") val nickName: String,
        @JsonProperty("phone_num") val phoneNum: String,
        val authority: Authority,
        val age: Age
)

fun toDto(user: User) : UserResDto {
    return UserResDto(user.id, user.email, user.nickName, user.phoneNum, user.authority, user.age)
}
