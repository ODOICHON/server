package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.Age
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User

class MockEntity {
    companion object {
        fun testUser1() = UserSignUpReqDto(
            email = "zzangu_jhouse_com",
            password = "abcdefG123!",
            nickName = "테스트유저1",
            phoneNum = "01011111111",
            age = "20대 미만",
            joinPaths = mutableListOf("네이버 카페", "인스타그램")
        )
        fun testUser1SignIn() = UserSignInReqDto (
            email = "zzangu_jhouse_com",
            password = "abcdefG123!",
        )
        fun testAdmin() = User(
            email = "IronSu_test_com",
            password = "passworD123!",
            nickName = "철수",
            phoneNum = "01098765432",
            authority = Authority.ADMIN,
            age = Age.TWENTY
        )



        fun testUserSignUpDto() = UserSignUpReqDto(
            email = "test_jhouse_com",
            password = "abcdefG123!",
            nickName = "테스트유저1",
            phoneNum = "01011111111",
            age = "20대 미만",
            joinPaths = mutableListOf("네이버 카페", "인스타그램")
        )

        fun testUserSignInDto() = UserSignInReqDto(
            email = "test_jhouse_com",
            password = "abcdefG123!"
        )

        fun testEmailDto() = EmailReqDto(
            email = "test_jhouse_com"
        )

        fun testNickNameDto() = NickNameReqDto(
            nickName = "testuser"
        )

        fun testPhoneNumDto() = PhoneNumReqDto(
            phoneNum = "01011111111"
        )

        fun testPasswordDto() = PasswordReqDto(
            password = "abcdFGH123!"
        )


    }
}