package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.UserSignUpReqDto
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User

class MockEntity {
    companion object {
        fun testUser1() = User(
                email = "ZZang9@test.com",
                password = "password123!",
                nickName = "짱구",
                phoneNum = "010-1234-5678",
                authority = Authority.USER
        )

        fun testUser2() = User(
                email = "IronSu@test.com",
                password = "password123!",
                nickName = "철수",
                phoneNum = "010-9876-5432",
                authority = Authority.USER
        )

        fun testPost1(writer : User) = Post(
                code = "<html>짱구가 태그 사이에 갇혔다.</html>",
                title = "짱구는 못말려",
                category = PostCategory.DAILY,
                imageUrls = emptyList(),
                isSaved = true,
                user = writer,
                love = 1
        )

        fun testUserSignUpDto() = UserSignUpReqDto(
                email = "test@jhouse.com",
                password = "abcdefg123!",
                nickName = "테스트유저1",
                phoneNum = "01011111111"
        )

        fun testUserSignInDto() = UserSignInReqDto(
                email = "test@jhouse.com",
                password = "abcdefg123!"
        )

    }
}