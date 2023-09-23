package com.example.jhouse_server.domain.user

import com.example.jhouse_server.domain.user.entity.Age
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class UserSignUpReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "아이디 형식에 맞지 않습니다.")
        val userName: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[A-Za-z0-9가-힣]{1,20}\$", message = "닉네임 형식에 맞지 않습니다.")
        @JsonProperty("nick_name") val nickName: String,
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}", message = "전화번호 형식에 맞지 않습니다.")
        @JsonProperty("phone_num") val phoneNum: String,
        @NotNull val age: String,
        @NotNull @JsonProperty("join_paths") val joinPaths: MutableList<String>,
        @NotNull @JsonProperty("terms") val terms: MutableList<String>,
        @field:Email(message = "이메일 형식에 맞지 않습니다.") @NotNull
        val email : String,
)

data class AgentSignUpReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "아이디 형식에 맞지 않습니다.")
        val userName: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z0-9가-힣])[A-Za-z0-9가-힣]{1,20}\$", message = "닉네임 형식에 맞지 않습니다.")
        @JsonProperty("nick_name") val nickName: String,
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}", message = "전화번호 형식에 맞지 않습니다.")
        @JsonProperty("phone_num") val phoneNum: String,
        @NotNull val age: String,
        @NotNull @JsonProperty("join_paths") val joinPaths: MutableList<String>,
        @NotNull @JsonProperty("terms") val terms: MutableList<String>,

        @field:Pattern(regexp = "^[0-9,-]{14}\$", message = "공인중개사 등록번호 형식에 맞지 않습니다.")
        @JsonProperty("agent_code") val agentCode: String,
        @field:Pattern(regexp = "^[0-9,-]{10}\$", message = "사업자 등록번호 형식에 맞지 않습니다.")
        @JsonProperty("business_code") val businessCode: String,
        @NotNull @JsonProperty("company_name") val companyName: String,
        @NotNull @JsonProperty("agent_name") val agentName: String,
        @field:Pattern(regexp = "^0(?:2|3[1-3]|4[1-3]|5[1-5]|6[1-4])[0-9]{7,8}", message = "사무소 번호 형식에 맞지 않습니다.")
        @NotNull @JsonProperty("company_phone_num") val companyPhoneNum: String,
        @JsonProperty("assistant_name") val assistantName: String?,
        @NotNull @JsonProperty("company_address") val companyAddress: String,
        @NotNull @JsonProperty("company_address_detail") val companyAddressDetail: String,
        @field:Email(message = "이메일 형식에 맞지 않습니다.") @NotNull
        @JsonProperty("company_email") val companyEmail: String,
        @NotNull val estate: String,
)

data class UserSignInReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "이메일 형식에 맞지 않습니다.")
        val userName: String,
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String
)

data class UserUpdateReqDto(
        @field:Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$", message = "비밀번호 형식에 맞지 않습니다.")
        val password: String,
        @JsonProperty("nick_name")
        val nickName: String?,
        @JsonProperty("new_password")
        val newPassword: String?,
        @JsonProperty("phone_num")
        val phoneNum: String?
)

data class CheckSmsReqDto(
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}", message = "전화번호 형식에 맞지 않습니다.")
        @JsonProperty("phone_num") val phoneNum: String,
        @field:Pattern(regexp = "^[0-9]{4}", message = "인증번호 형식에 맞지 않습니다.")
        val code: String
)

data class CheckEmailReqDto(
        @field:Email(message = "이메일 형식에 맞지 않습니다.") @NotNull
        val email: String,
        @field:Pattern(regexp = "^[0-9]{4}", message = "인증번호 형식에 맞지 않습니다.")
        val code: String
)

data class EmailReqDto(
        @field:Email(message = "이메일 형식에 맞지 않습니다.") @NotNull
        val email: String
)

data class UserNameReqDto(
        @field:Pattern(regexp = "^(?=.*[A-Za-z])[A-Za-z_0-9]{4,20}\$", message = "아이디 형식에 맞지 않습니다.")
        val userName: String
)

data class PhoneNumReqDto(
        @field:Pattern(regexp = "^01(?:0|1|[6-9])[0-9]{7,8}$", message = "전화번호 형식에 맞지 않습니다.")
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
        val userName: String,
        @JsonProperty("nick_name") val nickName: String,
        @JsonProperty("phone_num") val phoneNum: String,
        val authority: Authority,
        val age: Age,
        @JsonProperty("profile_image_url")
        val profileImageUrl: String,
        val userType: UserType,
        val email : String
)

data class WithdrawalUserReqDto(
        @NotNull
        val reason: List<String>,
        val content: String?
)

data class WithdrawalUser (
        val nickname: String = "탈퇴한 회원"
)

data class DefaultUser (
        val profileImageUrl: String = "https://duaily-content.s3.ap-northeast-2.amazonaws.com/default_profile_image.png"
)

fun toDto(user: User) : UserResDto {
    return UserResDto(user.id, user.userName, user.nickName, user.phoneNum, user.authority, user.age, user.profileImageUrl, user.userType, user.email)
}
