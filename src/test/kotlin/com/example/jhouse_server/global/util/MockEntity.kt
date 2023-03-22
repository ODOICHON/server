package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.board.BoardReqDto
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
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

        fun boardReqDto() = BoardReqDto(
            title = "짱구는 못말려",
            code = "<body> <div> <h2>짱구는 못말려</h2> </div> <div> <i>철수</i>야 나랑 놀자 </div> </body>",
            category = BoardCategory.TREND,
            imageUrls = mutableListOf("img001"),
            saved = true,
            prefixCategory = PrefixCategory.INTRO,
            fixed = false
        )

        fun commentReqDto(board: Long) = CommentCreateReqDto(
            boardId = board,
            content = "짱구야, 공부 하자."
        )

        fun boardIntroReqDto() = BoardReqDto(
            title = "오도이촌 소개 게시글",
            code = "<body> <div> <h2>오도이촌 소개를 해보겠습니다. </h2> </div> <div> <br/> <b>안녕</b> 하세요. 오도리입니다.</div> </body>",
            category = BoardCategory.REVIEW,
            imageUrls = mutableListOf("img002, img003"),
            saved = true,
            prefixCategory = PrefixCategory.INTRO,
            fixed = false
        )
        fun boardDefaultReqDto() = BoardReqDto(
            title = "짱구가 쓰는 자유 게시글",
            code = "<body> <div> <h2>주먹밥 머리 훈이는 내 친구. </h2> </div> <div> <br/> <b>안녕</b> 하세요. 짱구에요.</div> </body>",
            category = BoardCategory.DAILY,
            imageUrls = mutableListOf("img004, img005"),
            saved = true,
            prefixCategory = PrefixCategory.DEFAULT,
            fixed = false
        )
        fun boardAdsReqDto() = BoardReqDto(
            title = "훈이가 쓰는 홍보 게시글",
            code = "<body> <div> <h2>감자머리 짱구는 내 친구. </h2> </div> <div> <br/> <b>안녕</b> 하세요. 훈이에요.</div> </body>",
            category = BoardCategory.INTERIOR,
            imageUrls = mutableListOf("img006, img007"),
            saved = true,
            prefixCategory = PrefixCategory.ADVERTISEMENT,
            fixed = true
        )
    }
}