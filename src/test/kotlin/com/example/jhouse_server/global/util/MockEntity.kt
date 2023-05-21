package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.board.BoardReqDto
import com.example.jhouse_server.domain.board.BoardUpdateReqDto
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.comment.dto.CommentReqDto
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordReqDto
import com.example.jhouse_server.domain.record.dto.RecordUpdateDto
import com.example.jhouse_server.domain.record_category.dto.TemplateUpdateReqDto
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentReqDto
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentUpdateDto
import com.example.jhouse_server.domain.record_review.dto.RecordReviewReqDto
import com.example.jhouse_server.domain.record_review.dto.RecordReviewUpdateDto
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

        fun testUserSignUpDto2() = UserSignUpReqDto(
            email = "test2_jhouse_com",
            password = "abcdefG123!",
            nickName = "테스트유저2",
            phoneNum = "01022222222",
            age = "20대 미만",
            joinPaths = mutableListOf("네이버 카페", "인스타그램")
        )

        fun testUserSignUpDto3() = UserSignUpReqDto(
            email = "test3_jhouse_com",
            password = "abcdefG123!",
            nickName = "테스트유저3",
            phoneNum = "01033333333",
            age = "20대 미만",
            joinPaths = mutableListOf("네이버 카페", "인스타그램")
        )

        fun testUserSignInDto() = UserSignInReqDto(
            email = "test_jhouse_com",
            password = "abcdefG123!"
        )

        fun testUserSignInDtoEx1() = UserSignInReqDto(
            email = "test_jhouse_com1",
            password = "abcdefG123!"
        )

        fun testUserSignInDtoEx2() = UserSignInReqDto(
            email = "test_jhouse_com",
            password = "abcdefg123!"
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
            prefixCategory = PrefixCategory.INTRO,
            fixed = false
        )
        fun boardUpdateReqDto() = BoardUpdateReqDto(
            title = "짱구는 못말려",
            code = "<body> <div> <h2>짱구는 못말려</h2> </div> <div> <i>철수</i>야 나랑 놀자 </div> </body>",
            category = "REVIEW",
            imageUrls = mutableListOf("img001"),
            prefixCategory = "INTRO",
            fixed = false
        )

        fun commentReqDto(board: Long) = CommentReqDto(
            boardId = board,
            content = "짱구야, 공부 하자."
        )

        fun updateCommentReqDto(board: Long) = CommentReqDto(
            boardId = board,
            content = "철수야, 축구 하자~"
        )

        fun boardIntroReqDto() = BoardReqDto(
            title = "오도이촌 소개 게시글",
            code = "<body> <div> <h2>오도이촌 소개를 해보겠습니다. </h2> </div> <div> <br/> <b>안녕</b> 하세요. 오도리입니다.</div> </body>",
            category = BoardCategory.REVIEW,
            imageUrls = mutableListOf("img002, img003"),
            prefixCategory = PrefixCategory.INTRO,
            fixed = false
        )
        fun boardDefaultReqDto() = BoardReqDto(
            title = "짱구가 쓰는 자유 게시글",
            code = "<body> <div> <h2>주먹밥 머리 훈이는 내 친구. </h2> </div> <div> <br/> <b>안녕</b> 하세요. 짱구에요.</div> </body>",
            category = BoardCategory.DAILY,
            imageUrls = mutableListOf("img004, img005"),
            prefixCategory = PrefixCategory.DEFAULT,
            fixed = false
        )
        fun boardAdsReqDto() = BoardReqDto(
            title = "훈이가 쓰는 홍보 게시글",
            code = "<body> <div> <h2>감자머리 짱구는 내 친구. </h2> </div> <div> <br/> <b>안녕</b> 하세요. 훈이에요.</div> </body>",
            category = BoardCategory.INTERIOR,
            imageUrls = mutableListOf("img006, img007"),
            prefixCategory = PrefixCategory.ADVERTISEMENT,
            fixed = true
        )

        fun comment(findBoard: Board, user: User): Comment {
            return Comment(findBoard, "댓글이란다.", user)
        }

        fun templateSaveReqDto() = TemplateUpdateReqDto(
            category = "팀 내 문화",
            template = "제목 : \n내용 : "
        )

        fun templateUpdateReqDto() = TemplateUpdateReqDto(
            category = "팀 내 문화",
            template = "제목 : \n내용 : \n작성자 : "
        )

        fun odoriReqDto() = RecordReqDto(
            title = "코드 리뷰 문화 도입",
            content = "pr 날린 코드에 대해 팀원들이 리뷰해줍니다.",
            part = "server",
            category = "팀 내 문화",
            dType = "odori"
        )

        fun retroReqDto() = RecordReqDto(
            title = "DDOS 대응 회고",
            content = "DDOS 공격을 방지하기 위해 Bucket4j 라이브러리를 도입했습니다.",
            part = "server",
            category = "회고",
            dType = "retro"
        )

        fun techReqDtoNewTech() = RecordReqDto(
            title = "인증 인가 직접 구현하기",
            content = "스프링 시큐리티를 사용하지 않고, 인증 인가를 직접 구현했습니다.",
            part = "server",
            category = "신 기술",
            dType = "tech"
        )

        fun techReqDtoIssue() = RecordReqDto(
            title = "이슈 제목",
            content = "이슈 내용입니다.",
            part = "server",
            category = "신 기술",
            dType = "tech"
        )

        fun recordUpdateDto() = RecordUpdateDto(
            title = "수정 제목",
            content = "수정 내용"
        )

        fun recordPageConditionAll() = RecordPageCondition(
            part = "all",
            dType = "all",
            category = ""
        )

        fun recordPageConditionOdori() = RecordPageCondition(
            part = "all",
            dType = "odori",
            category = "culture"
        )

        fun recordPageConditionRetro() = RecordPageCondition(
            part = "all",
            dType = "retro",
            category = "retrospection"
        )

        fun recordPageConditionTech() = RecordPageCondition(
            part = "all",
            dType = "tech",
            category = "new_tech"
        )

        fun recordReviewReqDto(recordId: Long, status: String) = RecordReviewReqDto(
            recordId = recordId,
            content = "좋은 글 감사합니다!",
            status = status
        )

        fun recordReviewUpdateDto() = RecordReviewUpdateDto(
            content = "수정 내용",
            status = "approve"
        )

        fun recordCommentReqDto(recordId: Long, parentId: Long?) = RecordCommentReqDto(
            recordId = recordId,
            parentId = parentId,
            content = "댓글입니다."
        )

        fun recordCommentUpdateDto() = RecordCommentUpdateDto(
            content = "수정 내용"
        )
    }
}