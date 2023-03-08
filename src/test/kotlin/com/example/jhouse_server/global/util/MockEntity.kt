package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.ads.dto.AdsPostCreateReqDto
import com.example.jhouse_server.domain.ads.entity.AdsPostCategory
import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.UserSignUpReqDto
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User

class MockEntity {
    companion object {
        fun testUser1() = UserSignUpReqDto(
            email = "zzangu@jhouse.com",
            password = "abcdefg123!",
            nickName = "테스트유저1",
            phoneNum = "01011111111"
        )
        fun testUser1SignIn() = UserSignInReqDto (
            email = "zzangu@jhouse.com",
            password = "abcdefg123!",
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
        fun testAdsPostDto() = AdsPostCreateReqDto(
            code = "<html><body><h2>안<b>녕</b>하세요.</h2> 저는 <i>짱구</i>라고해요. <br/>만나서 반가워요.</body></html>",
            title = "짱구가 작성하는 게시글",
            imageUrls = arrayOf("img001", "img002").toList(),
            true,
            category = AdsPostCategory.INTERIOR
        )
        fun testAdsTmpPostDto() = AdsPostCreateReqDto(
            code = "<html><body><h2>안<b>녕</b>하세요.</h2> 저는 <i>짱구</i>라고해요. <br/>만나서 반가워요.</body></html>",
            title = "짱구가 작성하는 게시글",
            imageUrls = arrayOf("img001", "img002").toList(),
            false,
            category = AdsPostCategory.INTERIOR
        )
    }
}