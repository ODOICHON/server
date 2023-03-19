package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.ads.dto.AdsPostCreateReqDto
import com.example.jhouse_server.domain.ads.dto.AdsPostUpdateReqDto
import com.example.jhouse_server.domain.ads.entity.AdsPostCategory
import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.post.entity.PostCategory
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

        fun testAdsPostDto() = AdsPostCreateReqDto(
            code = "<html><body><h2>안<b>녕</b>하세요.</h2> 저는 <i>짱구</i>라고해요. <br/>만나서 반가워요.</body></html>",
            title = "짱구가 작성하는 게시글",
            imageUrls = arrayOf("img001", "img002").toList(),
            true,
            category = AdsPostCategory.INTERIOR
        )
        fun testAdsPostUpdateDto() = AdsPostUpdateReqDto(
            code = "<html><body><h2>안<b>녕</b>하세요.</h2> 저는 <i>철수</i>라고해요. <br/>만나서 반가워요.</body></html>",
            title = "철수가 작성하는 게시글",
            imageUrls = arrayOf("img001", "img002").toList(),
            true,
            category = "INTERIOR"
        )
        fun testAdsTmpPostDto() = AdsPostCreateReqDto(
            code = "<html><body><h2>임<b>시</b>작성 중입니다.</h2> 저를 <i>찾지</i>말아주세요. <br/>만나서 반가워요.</body></html>",
            title = "짱구가 작성하는 임시 게시글",
            imageUrls = arrayOf("img001", "img002").toList(),
            false,
            category = AdsPostCategory.INTERIOR
        )
    }
}