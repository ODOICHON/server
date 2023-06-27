package com.example.jhouse_server.global.util

import com.example.jhouse_server.domain.board.BoardReqDto
import com.example.jhouse_server.domain.board.BoardUpdateReqDto
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.comment.dto.CommentReqDto
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.house.dto.HouseReqDto
import com.example.jhouse_server.domain.house.dto.ReportReqDto
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.RentalType
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
import com.example.jhouse_server.domain.user.entity.UserType

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
            age = Age.TWENTY,
            userType = UserType.NONE
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

        fun testAgentSignUpDto() = AgentSignUpReqDto(
            email = "agent_jhouse_com",
            password = "abcdefG123!",
            nickName = "공인중개사",
            phoneNum = "01044444444",
            age = "20대 미만",
            joinPaths = mutableListOf("네이버 카페", "인스타그램"),
            agentCode = "123-456-789-01",
            businessCode = "12345-6789",
            companyName = "주말내집",
            agentName = "오도리",
            companyPhoneNum = "0212345678",
            assistantName = null,
            companyAddress = "서울특별시",
            companyAddressDetail = "강남구",
            companyEmail = "agent@duaily.net",
            estate = "아파트"
        )

        fun testUserSignInDto() = UserSignInReqDto(
            email = "test_jhouse_com",
            password = "abcdefG123!"
        )

        fun testUserSignInDto2() = UserSignInReqDto(
            email = "test2_jhouse_com",
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
            category = "culture",
            template = "제목 : \n내용 : "
        )

        fun templateSaveExDto() = TemplateUpdateReqDto(
            category = "exception",
            template = "제목 : \n내용 : "
        )

        fun templateUpdateReqDto() = TemplateUpdateReqDto(
            category = "culture",
            template = "제목 : \n내용 : \n작성자 : "
        )

        fun odoriReqDto() = RecordReqDto(
            title = "코드 리뷰 문화 도입",
            content = "pr 날린 코드에 대해 팀원들이 리뷰해줍니다.",
            part = "server",
            category = "culture",
            type = "odori"
        )

        fun retroReqDto() = RecordReqDto(
            title = "DDOS 대응 회고",
            content = "DDOS 공격을 방지하기 위해 Bucket4j 라이브러리를 도입했습니다.",
            part = "server",
            category = "retrospection",
            type = "retro"
        )

        fun techReqDtoNewTech() = RecordReqDto(
            title = "인증 인가 직접 구현하기",
            content = "스프링 시큐리티를 사용하지 않고, 인증 인가를 직접 구현했습니다.",
            part = "server",
            category = "new_tech",
            type = "tech"
        )

        fun techReqDtoIssue() = RecordReqDto(
            title = "이슈 제목",
            content = "이슈 내용입니다.",
            part = "server",
            category = "issue",
            type = "tech"
        )

        fun recordUpdateDto() = RecordUpdateDto(
            title = "수정 제목",
            content = "수정 내용"
        )

        fun recordPageConditionAll() = RecordPageCondition(
            part = "all",
            type = "all",
            category = ""
        )

        fun recordPageConditionOdori() = RecordPageCondition(
            part = "all",
            type = "odori",
            category = "culture"
        )

        fun recordPageConditionRetro() = RecordPageCondition(
            part = "all",
            type = "retro",
            category = "retrospection"
        )

        fun recordPageConditionTech() = RecordPageCondition(
            part = "all",
            type = "tech",
            category = "new_tech"
        )

        fun recordPageCondition(part: String, type: String, category: String) = RecordPageCondition(
            part = part,
            type = type,
            category = category
        )

        fun recordReviewReqDto(recordId: Long, status: String) = RecordReviewReqDto(
            recordId = recordId,
            content = "좋은 글 감사합니다!",
            status = status
        )

        fun recordReviewReqDtoAll(recordId: Long, content: String, status: String) = RecordReviewReqDto(
            recordId = recordId,
            content = content,
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

        fun houseReqDto() = HouseReqDto(
            rentalType = RentalType.SALE,
            city = "서울시 서대문구 남가좌동 거북골로 34",
            zipCode = "12345",
            size = "120909000",
            purpose = "주거용 ( 방3, 화장실 2 )",
            floorNum = 2,
            contact = "070-1234-5678",
            createdDate = "1990-09-09",
            price = 12000,
            monthlyPrice = 0.0,
            agentName = "행복부동산",
            title = "행복부동산에서 제공하는 서울시 빈집 매물입니다.",
            code = "<body> <div> <h2>행복부동산 최신 매물로</h2> </div> <div> <i>주거용 주택</i>을 소개합니다. </div> </body>",
            imageUrls = mutableListOf("img-001", "img-002"),
            tmpYn = false
            )
        fun houseUpdateReqDto() = HouseReqDto(
            rentalType = RentalType.JEONSE,
            city = "서울시 서대문구 남가좌동 거북골로 90",
            zipCode = "12345",
            size = "120909000",
            purpose = "게스트하우스 ( 방3, 화장실 2 )",
            floorNum = 2,
            contact = "070-1234-5678",
            createdDate = "1990-09-09",
            price = 12000,
            monthlyPrice = 0.0,
            agentName = "부자부동산",
            title = "부자부동산에서 제공하는 서울시 빈집 매물입니다.",
            code = "<body> <div> <h2>부자부동산 최신 매물로</h2> </div> <div> <i>게스트하우스용 주택</i>을 소개합니다. </div> </body>",
            imageUrls = mutableListOf("img-001", "img-002"),
            tmpYn = false
        )

        fun reportReqDto() = ReportReqDto(
            reportReason = "허위 매물로 게시글을 작성하였습니다."
        )

        fun houseTmpReqDto() = HouseReqDto(
            rentalType = RentalType.SALE,
            city = "서울시 서대문구 남가좌동 거북골로 34",
            zipCode = "12345",
            size = "120909000",
            purpose = "주거용 ( 방3, 화장실 2 )",
            floorNum = 2,
            contact = "070-1234-5678",
            createdDate = "1990-09-09",
            price = 12000,
            monthlyPrice = 0.0,
            agentName = "행복부동산",
            title = "행복부동산에서 제공하는 서울시 빈집 매물입니다.",
            code = "<body> <div> <h2>행복부동산 최신 매물로</h2> </div> <div> <i>주거용 주택</i>을 소개합니다. </div> </body>",
            imageUrls = mutableListOf("img-001", "img-002"),
            tmpYn = true
        )

        fun houseTooLongReqDto() = HouseReqDto(
            rentalType = RentalType.SALE,
            city = "서울시 서대문구 남가좌동 거북골로 34",
            zipCode = "12345",
            size = "120909000",
            purpose = "주거용 ( 방3, 화장실 2 )",
            floorNum = 2,
            contact = "070-1234-5678",
            createdDate = "1990-09-09",
            price = 12000,
            monthlyPrice = 0.0,
            agentName = "행복부동산",
            title = "행복부동산에서 제공하는 서울시 빈집 매물입니다.",
            code = "<body> <div> <h2>글자수가 10,000자를 넘어서는지 테스트해보기 위한 글입니다. 행복부동산 최신 매물입니다.</h2> </div> <div> " +
                    "오도이촌, 세컨하우스를 꿈꾸고 있는 여러분, 오도이촌으로 어떤 지역을 꿈꾸고 계시나요?\n" +
                    "오도이촌을 위해 세컨하우스를 택할 지역을 고를 때 가장 중요한 기준으로는 ‘편리한 교통’, ‘수려한 자연환경’, ‘뛰어난 편의시설’ 등이 있는데요.\n" +
                    "이번 컨텐츠에서는 어떤 지역이 여러분의 세컨하우스 기준을 충족하는 지역일지 제시해 드리고자 합니다.\n" +
                    "경기도\n" +
                    "도심과 가까운 지역, 양평\n" +
                    "양평은 도심과 가깝고 교통이 좋아 오도이촌으로 특히 인기가 많은 지역입니다.\n" +
                    "1) 용이한 교통\n" +
                    "양평은 서울에서 차로 약 50분 거리에 위치해있는데요. 경의중앙선 용문역, 6번 국도 등 교통 인프라가 잘 갖추어져 있습니다. 인근에는 서울 송파~양평 고속도로 개통(2023년 예정)도 앞두고 있어 교통환경이 더욱 개선될 전망입니다.\n" +
                    "2) 관광지/자연환경\n" +
                    "양평은 용문 5일장, 용문사, 중원계곡, 용문 관광단지 등으로도 유명합니다. 볼거리와 먹을거리, 놀거리가 많고 레저활동을 하기 좋은 여건을 갖추고 있습니다. 또한 용문산과 북한강이 어우러진 입지로, 하늘과 숲과 물이 하나가 되는 자연환경을 누릴 수 있습니다.\n" +
                    "3) 문화시설/편의시설\n" +
                    "자연경관과 공기가 좋은 곳 뿐만 아니라 문화시설/편의시설이 있는 곳도 중요합니다. 시골은 특히나 대형마트가 부족한데 양평은 비교적 대형마트와 생활 편의시설이 많은 곳에 속합니다.\n" +
                    "세컨하우스를 찾는 사람들의 양평 대표 지역은 서종면, 옥천면, 용문면, 강하면이 대표적입니다. 양평 중에서도 문화공간이 가까이 있는 곳은 ‘서종면’ 쪽입니다.\n" +
                    "양평과 비슷한 지역으로는 경기도 광주, 가평 등이 있습니다. 매경이코노미가 부동산 전문가, 금융권 프라이빗 뱅커(PB) 30인을 대상으로 설문조사한 결과 세컨드하우스를 마련하기 좋은 곳 1위로 ‘경기도 양평’이 꼽혔다. 복수응답 기준으로 총 20표를 얻었습니다. 이어 2위는 ‘가평(12표)’, 3위는 ‘남양주(7표)’로 경기도 소재 시군이 1~3위를 휩쓸었다고 합니다.\n" +
                    "강원도\n" +
                    "비과세 혜택\n" +
                    "부동산 업계에 따르면 규제의 사각지대에 있고 교통여건의 개선으로 수도권과 접근성이 좋은 강원도의 장점이 외지인들의 세컨하우스 투자로 이어지면서, 매도자 우위 시장이 유지되고 있다고 합니다. 강원도 내 18개 시, 군, 구는 모두 비규제지역으로, LTV(주택담보대출비율)이 무주택자 기준 최대 70%까지 적용되어 타운하우스나 아파트를 마련하기가 상대적으로 유리합니다.\n" +
                    "강원도는 바다, 강, 산, 뷰, 관광지 등 휴가를 누리기에 최적인 지역인데 최근 고속도로와 KTX, 관광지 개발 사업 등의 영향으로 향후 시세차익을 기대하는 투자자들까지 몰려 세컨하우스의 가치가 높아지고 있다고 합니다.\n" +
                    "철도호재\n" +
                    "삼척~동해~강릉 구간 ‘동해선’과 동해 신항~삼척해변 구간 ‘동해신항선’, 용문-흥천 간 철도, 원주-만종 간 철도 등 강원 지역 신규 사업으로 반영되었습니다.\n" +
                    "세컨하우스 매물이 많은 속초, 강릉\n" +
                    "국내 대표 관광지인 속초는 2018년 평창올림픽 개최를 계기로 2017년 서울~양양고속도로 전 구간이 개통하면서 서울 접근성이 우수한 관광지역으로 떠오른 지역입니다.\n" +
                    "1) 관광 유입\n" +
                    "특히 최근에는 국내에서 유일하게 해변에 위치한 대관람차 ‘속초아이’가 오픈해 관광명소로서의 입지를 굳건히 하고 있습니다.\n" +
                    "2) 교통 개선\n" +
                    "속초는 동해북부선, 동서고속화철도 등 교통호재도 이어지고 있는데요. 서울~순천~속초를 잇는 동서고속화철도의 경우 2027년 개통을 앞두고 있습니다. KTX 속초역 개통과 동시에 향후 서울에서 속초까지 약 1시간대에 이동이 가능해질 전망입니다. 더불어 강릉선 KTX 이용객까지 더할 수 있어, 동해안 관광 유입도 기대할 수 있습니다.\n" +
                    "해외\n" +
                    "세컨하우스로 꼭 국내로만 고집할 필요는 없는데요. 멀지 않은 중국, 일본, 동남아 지역은 주말여행으로 충분히 다녀올 수 있기 때문입니다. 외국에서는 필리핀 세부&마닐라, 말레이시아 조호르바루 등이 꼽혔습니다.\n" +
                    "정말 다양한 지역이 있는데요. 이번 주말에는 자신에게 맞는 지역을 한 번 비교해 보며 찾아보는 것은 어떨까요?" +
                    "오도이촌, 세컨하우스를 꿈꾸고 있는 여러분, 오도이촌으로 어떤 지역을 꿈꾸고 계시나요?\n" +
                    "오도이촌을 위해 세컨하우스를 택할 지역을 고를 때 가장 중요한 기준으로는 ‘편리한 교통’, ‘수려한 자연환경’, ‘뛰어난 편의시설’ 등이 있는데요.\n" +
                    "이번 컨텐츠에서는 어떤 지역이 여러분의 세컨하우스 기준을 충족하는 지역일지 제시해 드리고자 합니다.\n" +
                    "경기도\n" +
                    "도심과 가까운 지역, 양평\n" +
                    "양평은 도심과 가깝고 교통이 좋아 오도이촌으로 특히 인기가 많은 지역입니다.\n" +
                    "1) 용이한 교통\n" +
                    "양평은 서울에서 차로 약 50분 거리에 위치해있는데요. 경의중앙선 용문역, 6번 국도 등 교통 인프라가 잘 갖추어져 있습니다. 인근에는 서울 송파~양평 고속도로 개통(2023년 예정)도 앞두고 있어 교통환경이 더욱 개선될 전망입니다.\n" +
                    "2) 관광지/자연환경\n" +
                    "양평은 용문 5일장, 용문사, 중원계곡, 용문 관광단지 등으로도 유명합니다. 볼거리와 먹을거리, 놀거리가 많고 레저활동을 하기 좋은 여건을 갖추고 있습니다. 또한 용문산과 북한강이 어우러진 입지로, 하늘과 숲과 물이 하나가 되는 자연환경을 누릴 수 있습니다.\n" +
                    "3) 문화시설/편의시설\n" +
                    "자연경관과 공기가 좋은 곳 뿐만 아니라 문화시설/편의시설이 있는 곳도 중요합니다. 시골은 특히나 대형마트가 부족한데 양평은 비교적 대형마트와 생활 편의시설이 많은 곳에 속합니다.\n" +
                    "세컨하우스를 찾는 사람들의 양평 대표 지역은 서종면, 옥천면, 용문면, 강하면이 대표적입니다. 양평 중에서도 문화공간이 가까이 있는 곳은 ‘서종면’ 쪽입니다.\n" +
                    "양평과 비슷한 지역으로는 경기도 광주, 가평 등이 있습니다. 매경이코노미가 부동산 전문가, 금융권 프라이빗 뱅커(PB) 30인을 대상으로 설문조사한 결과 세컨드하우스를 마련하기 좋은 곳 1위로 ‘경기도 양평’이 꼽혔다. 복수응답 기준으로 총 20표를 얻었습니다. 이어 2위는 ‘가평(12표)’, 3위는 ‘남양주(7표)’로 경기도 소재 시군이 1~3위를 휩쓸었다고 합니다.\n" +
                    "강원도\n" +
                    "비과세 혜택\n" +
                    "부동산 업계에 따르면 규제의 사각지대에 있고 교통여건의 개선으로 수도권과 접근성이 좋은 강원도의 장점이 외지인들의 세컨하우스 투자로 이어지면서, 매도자 우위 시장이 유지되고 있다고 합니다. 강원도 내 18개 시, 군, 구는 모두 비규제지역으로, LTV(주택담보대출비율)이 무주택자 기준 최대 70%까지 적용되어 타운하우스나 아파트를 마련하기가 상대적으로 유리합니다.\n" +
                    "강원도는 바다, 강, 산, 뷰, 관광지 등 휴가를 누리기에 최적인 지역인데 최근 고속도로와 KTX, 관광지 개발 사업 등의 영향으로 향후 시세차익을 기대하는 투자자들까지 몰려 세컨하우스의 가치가 높아지고 있다고 합니다.\n" +
                    "철도호재\n" +
                    "삼척~동해~강릉 구간 ‘동해선’과 동해 신항~삼척해변 구간 ‘동해신항선’, 용문-흥천 간 철도, 원주-만종 간 철도 등 강원 지역 신규 사업으로 반영되었습니다.\n" +
                    "세컨하우스 매물이 많은 속초, 강릉\n" +
                    "국내 대표 관광지인 속초는 2018년 평창올림픽 개최를 계기로 2017년 서울~양양고속도로 전 구간이 개통하면서 서울 접근성이 우수한 관광지역으로 떠오른 지역입니다.\n" +
                    "1) 관광 유입\n" +
                    "특히 최근에는 국내에서 유일하게 해변에 위치한 대관람차 ‘속초아이’가 오픈해 관광명소로서의 입지를 굳건히 하고 있습니다.\n" +
                    "2) 교통 개선\n" +
                    "속초는 동해북부선, 동서고속화철도 등 교통호재도 이어지고 있는데요. 서울~순천~속초를 잇는 동서고속화철도의 경우 2027년 개통을 앞두고 있습니다. KTX 속초역 개통과 동시에 향후 서울에서 속초까지 약 1시간대에 이동이 가능해질 전망입니다. 더불어 강릉선 KTX 이용객까지 더할 수 있어, 동해안 관광 유입도 기대할 수 있습니다.\n" +
                    "해외\n" +
                    "세컨하우스로 꼭 국내로만 고집할 필요는 없는데요. 멀지 않은 중국, 일본, 동남아 지역은 주말여행으로 충분히 다녀올 수 있기 때문입니다. 외국에서는 필리핀 세부&마닐라, 말레이시아 조호르바루 등이 꼽혔습니다.\n" +
                    "정말 다양한 지역이 있는데요. 이번 주말에는 자신에게 맞는 지역을 한 번 비교해 보며 찾아보는 것은 어떨까요?" +
                    "오도이촌, 세컨하우스를 꿈꾸고 있는 여러분, 오도이촌으로 어떤 지역을 꿈꾸고 계시나요?\n" +
                    "오도이촌을 위해 세컨하우스를 택할 지역을 고를 때 가장 중요한 기준으로는 ‘편리한 교통’, ‘수려한 자연환경’, ‘뛰어난 편의시설’ 등이 있는데요.\n" +
                    "이번 컨텐츠에서는 어떤 지역이 여러분의 세컨하우스 기준을 충족하는 지역일지 제시해 드리고자 합니다.\n" +
                    "경기도\n" +
                    "도심과 가까운 지역, 양평\n" +
                    "양평은 도심과 가깝고 교통이 좋아 오도이촌으로 특히 인기가 많은 지역입니다.\n" +
                    "1) 용이한 교통\n" +
                    "양평은 서울에서 차로 약 50분 거리에 위치해있는데요. 경의중앙선 용문역, 6번 국도 등 교통 인프라가 잘 갖추어져 있습니다. 인근에는 서울 송파~양평 고속도로 개통(2023년 예정)도 앞두고 있어 교통환경이 더욱 개선될 전망입니다.\n" +
                    "2) 관광지/자연환경\n" +
                    "양평은 용문 5일장, 용문사, 중원계곡, 용문 관광단지 등으로도 유명합니다. 볼거리와 먹을거리, 놀거리가 많고 레저활동을 하기 좋은 여건을 갖추고 있습니다. 또한 용문산과 북한강이 어우러진 입지로, 하늘과 숲과 물이 하나가 되는 자연환경을 누릴 수 있습니다.\n" +
                    "3) 문화시설/편의시설\n" +
                    "자연경관과 공기가 좋은 곳 뿐만 아니라 문화시설/편의시설이 있는 곳도 중요합니다. 시골은 특히나 대형마트가 부족한데 양평은 비교적 대형마트와 생활 편의시설이 많은 곳에 속합니다.\n" +
                    "세컨하우스를 찾는 사람들의 양평 대표 지역은 서종면, 옥천면, 용문면, 강하면이 대표적입니다. 양평 중에서도 문화공간이 가까이 있는 곳은 ‘서종면’ 쪽입니다.\n" +
                    "양평과 비슷한 지역으로는 경기도 광주, 가평 등이 있습니다. 매경이코노미가 부동산 전문가, 금융권 프라이빗 뱅커(PB) 30인을 대상으로 설문조사한 결과 세컨드하우스를 마련하기 좋은 곳 1위로 ‘경기도 양평’이 꼽혔다. 복수응답 기준으로 총 20표를 얻었습니다. 이어 2위는 ‘가평(12표)’, 3위는 ‘남양주(7표)’로 경기도 소재 시군이 1~3위를 휩쓸었다고 합니다.\n" +
                    "강원도\n" +
                    "비과세 혜택\n" +
                    "부동산 업계에 따르면 규제의 사각지대에 있고 교통여건의 개선으로 수도권과 접근성이 좋은 강원도의 장점이 외지인들의 세컨하우스 투자로 이어지면서, 매도자 우위 시장이 유지되고 있다고 합니다. 강원도 내 18개 시, 군, 구는 모두 비규제지역으로, LTV(주택담보대출비율)이 무주택자 기준 최대 70%까지 적용되어 타운하우스나 아파트를 마련하기가 상대적으로 유리합니다.\n" +
                    "강원도는 바다, 강, 산, 뷰, 관광지 등 휴가를 누리기에 최적인 지역인데 최근 고속도로와 KTX, 관광지 개발 사업 등의 영향으로 향후 시세차익을 기대하는 투자자들까지 몰려 세컨하우스의 가치가 높아지고 있다고 합니다.\n" +
                    "철도호재\n" +
                    "삼척~동해~강릉 구간 ‘동해선’과 동해 신항~삼척해변 구간 ‘동해신항선’, 용문-흥천 간 철도, 원주-만종 간 철도 등 강원 지역 신규 사업으로 반영되었습니다.\n" +
                    "세컨하우스 매물이 많은 속초, 강릉\n" +
                    "국내 대표 관광지인 속초는 2018년 평창올림픽 개최를 계기로 2017년 서울~양양고속도로 전 구간이 개통하면서 서울 접근성이 우수한 관광지역으로 떠오른 지역입니다.\n" +
                    "1) 관광 유입\n" +
                    "특히 최근에는 국내에서 유일하게 해변에 위치한 대관람차 ‘속초아이’가 오픈해 관광명소로서의 입지를 굳건히 하고 있습니다.\n" +
                    "2) 교통 개선\n" +
                    "속초는 동해북부선, 동서고속화철도 등 교통호재도 이어지고 있는데요. 서울~순천~속초를 잇는 동서고속화철도의 경우 2027년 개통을 앞두고 있습니다. KTX 속초역 개통과 동시에 향후 서울에서 속초까지 약 1시간대에 이동이 가능해질 전망입니다. 더불어 강릉선 KTX 이용객까지 더할 수 있어, 동해안 관광 유입도 기대할 수 있습니다.\n" +
                    "해외\n" +
                    "세컨하우스로 꼭 국내로만 고집할 필요는 없는데요. 멀지 않은 중국, 일본, 동남아 지역은 주말여행으로 충분히 다녀올 수 있기 때문입니다. 외국에서는 필리핀 세부&마닐라, 말레이시아 조호르바루 등이 꼽혔습니다.\n" +
                    "정말 다양한 지역이 있는데요. 이번 주말에는 자신에게 맞는 지역을 한 번 비교해 보며 찾아보는 것은 어떨까요?" +
                    "오도이촌, 세컨하우스를 꿈꾸고 있는 여러분, 오도이촌으로 어떤 지역을 꿈꾸고 계시나요?\n" +
                    "오도이촌을 위해 세컨하우스를 택할 지역을 고를 때 가장 중요한 기준으로는 ‘편리한 교통’, ‘수려한 자연환경’, ‘뛰어난 편의시설’ 등이 있는데요.\n" +
                    "이번 컨텐츠에서는 어떤 지역이 여러분의 세컨하우스 기준을 충족하는 지역일지 제시해 드리고자 합니다.\n" +
                    "경기도\n" +
                    "도심과 가까운 지역, 양평\n" +
                    "양평은 도심과 가깝고 교통이 좋아 오도이촌으로 특히 인기가 많은 지역입니다.\n" +
                    "1) 용이한 교통\n" +
                    "양평은 서울에서 차로 약 50분 거리에 위치해있는데요. 경의중앙선 용문역, 6번 국도 등 교통 인프라가 잘 갖추어져 있습니다. 인근에는 서울 송파~양평 고속도로 개통(2023년 예정)도 앞두고 있어 교통환경이 더욱 개선될 전망입니다.\n" +
                    "2) 관광지/자연환경\n" +
                    "양평은 용문 5일장, 용문사, 중원계곡, 용문 관광단지 등으로도 유명합니다. 볼거리와 먹을거리, 놀거리가 많고 레저활동을 하기 좋은 여건을 갖추고 있습니다. 또한 용문산과 북한강이 어우러진 입지로, 하늘과 숲과 물이 하나가 되는 자연환경을 누릴 수 있습니다.\n" +
                    "3) 문화시설/편의시설\n" +
                    "자연경관과 공기가 좋은 곳 뿐만 아니라 문화시설/편의시설이 있는 곳도 중요합니다. 시골은 특히나 대형마트가 부족한데 양평은 비교적 대형마트와 생활 편의시설이 많은 곳에 속합니다.\n" +
                    "세컨하우스를 찾는 사람들의 양평 대표 지역은 서종면, 옥천면, 용문면, 강하면이 대표적입니다. 양평 중에서도 문화공간이 가까이 있는 곳은 ‘서종면’ 쪽입니다.\n" +
                    "양평과 비슷한 지역으로는 경기도 광주, 가평 등이 있습니다. 매경이코노미가 부동산 전문가, 금융권 프라이빗 뱅커(PB) 30인을 대상으로 설문조사한 결과 세컨드하우스를 마련하기 좋은 곳 1위로 ‘경기도 양평’이 꼽혔다. 복수응답 기준으로 총 20표를 얻었습니다. 이어 2위는 ‘가평(12표)’, 3위는 ‘남양주(7표)’로 경기도 소재 시군이 1~3위를 휩쓸었다고 합니다.\n" +
                    "강원도\n" +
                    "비과세 혜택\n" +
                    "부동산 업계에 따르면 규제의 사각지대에 있고 교통여건의 개선으로 수도권과 접근성이 좋은 강원도의 장점이 외지인들의 세컨하우스 투자로 이어지면서, 매도자 우위 시장이 유지되고 있다고 합니다. 강원도 내 18개 시, 군, 구는 모두 비규제지역으로, LTV(주택담보대출비율)이 무주택자 기준 최대 70%까지 적용되어 타운하우스나 아파트를 마련하기가 상대적으로 유리합니다.\n" +
                    "강원도는 바다, 강, 산, 뷰, 관광지 등 휴가를 누리기에 최적인 지역인데 최근 고속도로와 KTX, 관광지 개발 사업 등의 영향으로 향후 시세차익을 기대하는 투자자들까지 몰려 세컨하우스의 가치가 높아지고 있다고 합니다.\n" +
                    "철도호재\n" +
                    "삼척~동해~강릉 구간 ‘동해선’과 동해 신항~삼척해변 구간 ‘동해신항선’, 용문-흥천 간 철도, 원주-만종 간 철도 등 강원 지역 신규 사업으로 반영되었습니다.\n" +
                    "세컨하우스 매물이 많은 속초, 강릉\n" +
                    "국내 대표 관광지인 속초는 2018년 평창올림픽 개최를 계기로 2017년 서울~양양고속도로 전 구간이 개통하면서 서울 접근성이 우수한 관광지역으로 떠오른 지역입니다.\n" +
                    "1) 관광 유입\n" +
                    "특히 최근에는 국내에서 유일하게 해변에 위치한 대관람차 ‘속초아이’가 오픈해 관광명소로서의 입지를 굳건히 하고 있습니다.\n" +
                    "2) 교통 개선\n" +
                    "속초는 동해북부선, 동서고속화철도 등 교통호재도 이어지고 있는데요. 서울~순천~속초를 잇는 동서고속화철도의 경우 2027년 개통을 앞두고 있습니다. KTX 속초역 개통과 동시에 향후 서울에서 속초까지 약 1시간대에 이동이 가능해질 전망입니다. 더불어 강릉선 KTX 이용객까지 더할 수 있어, 동해안 관광 유입도 기대할 수 있습니다.\n" +
                    "해외\n" +
                    "세컨하우스로 꼭 국내로만 고집할 필요는 없는데요. 멀지 않은 중국, 일본, 동남아 지역은 주말여행으로 충분히 다녀올 수 있기 때문입니다. 외국에서는 필리핀 세부&마닐라, 말레이시아 조호르바루 등이 꼽혔습니다.\n" +
                    "정말 다양한 지역이 있는데요. 이번 주말에는 자신에게 맞는 지역을 한 번 비교해 보며 찾아보는 것은 어떨까요?" +
                    "오도이촌, 세컨하우스를 꿈꾸고 있는 여러분, 오도이촌으로 어떤 지역을 꿈꾸고 계시나요?\n" +
                    "오도이촌을 위해 세컨하우스를 택할 지역을 고를 때 가장 중요한 기준으로는 ‘편리한 교통’, ‘수려한 자연환경’, ‘뛰어난 편의시설’ 등이 있는데요.\n" +
                    "이번 컨텐츠에서는 어떤 지역이 여러분의 세컨하우스 기준을 충족하는 지역일지 제시해 드리고자 합니다.\n" +
                    "경기도\n" +
                    "도심과 가까운 지역, 양평\n" +
                    "양평은 도심과 가깝고 교통이 좋아 오도이촌으로 특히 인기가 많은 지역입니다.\n" +
                    "1) 용이한 교통\n" +
                    "양평은 서울에서 차로 약 50분 거리에 위치해있는데요. 경의중앙선 용문역, 6번 국도 등 교통 인프라가 잘 갖추어져 있습니다. 인근에는 서울 송파~양평 고속도로 개통(2023년 예정)도 앞두고 있어 교통환경이 더욱 개선될 전망입니다.\n" +
                    "2) 관광지/자연환경\n" +
                    "양평은 용문 5일장, 용문사, 중원계곡, 용문 관광단지 등으로도 유명합니다. 볼거리와 먹을거리, 놀거리가 많고 레저활동을 하기 좋은 여건을 갖추고 있습니다. 또한 용문산과 북한강이 어우러진 입지로, 하늘과 숲과 물이 하나가 되는 자연환경을 누릴 수 있습니다.\n" +
                    "3) 문화시설/편의시설\n" +
                    "자연경관과 공기가 좋은 곳 뿐만 아니라 문화시설/편의시설이 있는 곳도 중요합니다. 시골은 특히나 대형마트가 부족한데 양평은 비교적 대형마트와 생활 편의시설이 많은 곳에 속합니다.\n" +
                    "세컨하우스를 찾는 사람들의 양평 대표 지역은 서종면, 옥천면, 용문면, 강하면이 대표적입니다. 양평 중에서도 문화공간이 가까이 있는 곳은 ‘서종면’ 쪽입니다.\n" +
                    "양평과 비슷한 지역으로는 경기도 광주, 가평 등이 있습니다. 매경이코노미가 부동산 전문가, 금융권 프라이빗 뱅커(PB) 30인을 대상으로 설문조사한 결과 세컨드하우스를 마련하기 좋은 곳 1위로 ‘경기도 양평’이 꼽혔다. 복수응답 기준으로 총 20표를 얻었습니다. 이어 2위는 ‘가평(12표)’, 3위는 ‘남양주(7표)’로 경기도 소재 시군이 1~3위를 휩쓸었다고 합니다.\n" +
                    "강원도\n" +
                    "비과세 혜택\n" +
                    "부동산 업계에 따르면 규제의 사각지대에 있고 교통여건의 개선으로 수도권과 접근성이 좋은 강원도의 장점이 외지인들의 세컨하우스 투자로 이어지면서, 매도자 우위 시장이 유지되고 있다고 합니다. 강원도 내 18개 시, 군, 구는 모두 비규제지역으로, LTV(주택담보대출비율)이 무주택자 기준 최대 70%까지 적용되어 타운하우스나 아파트를 마련하기가 상대적으로 유리합니다.\n" +
                    "강원도는 바다, 강, 산, 뷰, 관광지 등 휴가를 누리기에 최적인 지역인데 최근 고속도로와 KTX, 관광지 개발 사업 등의 영향으로 향후 시세차익을 기대하는 투자자들까지 몰려 세컨하우스의 가치가 높아지고 있다고 합니다.\n" +
                    "철도호재\n" +
                    "삼척~동해~강릉 구간 ‘동해선’과 동해 신항~삼척해변 구간 ‘동해신항선’, 용문-흥천 간 철도, 원주-만종 간 철도 등 강원 지역 신규 사업으로 반영되었습니다.\n" +
                    "세컨하우스 매물이 많은 속초, 강릉\n" +
                    "국내 대표 관광지인 속초는 2018년 평창올림픽 개최를 계기로 2017년 서울~양양고속도로 전 구간이 개통하면서 서울 접근성이 우수한 관광지역으로 떠오른 지역입니다.\n" +
                    "1) 관광 유입\n" +
                    "특히 최근에는 국내에서 유일하게 해변에 위치한 대관람차 ‘속초아이’가 오픈해 관광명소로서의 입지를 굳건히 하고 있습니다.\n" +
                    "2) 교통 개선\n" +
                    "속초는 동해북부선, 동서고속화철도 등 교통호재도 이어지고 있는데요. 서울~순천~속초를 잇는 동서고속화철도의 경우 2027년 개통을 앞두고 있습니다. KTX 속초역 개통과 동시에 향후 서울에서 속초까지 약 1시간대에 이동이 가능해질 전망입니다. 더불어 강릉선 KTX 이용객까지 더할 수 있어, 동해안 관광 유입도 기대할 수 있습니다.\n" +
                    "해외\n" +
                    "철도호재\n" +
                    "삼척~동해~강릉 구간 ‘동해선’과 동해 신항~삼척해변 구간 ‘동해신항선’, 용문-흥천 간 철도, 원주-만종 간 철도 등 강원 지역 신규 사업으로 반영되었습니다.\n" +
                    "세컨하우스 매물이 많은 속초, 강릉\n" +
                    "국내 대표 관광지인 속초는 2018년 평창올림픽 개최를 계기로 2017년 서울~양양고속도로 전 구간이 개통하면서 서울 접근성이 우수한 관광지역으로 떠오른 지역입니다.\n" +
                    "1) 관광 유입\n" +
                    "특히 최근에는 국내에서 유일하게 해변에 위치한 대관람차 ‘속초아이’가 오픈해 관광명소로서의 입지를 굳건히 하고 있습니다.\n" +
                    "2) 교통 개선\n" +
                    "속초는 동해북부선, 동서고속화철도 등 교통호재도 이어지고 있는데요. 서울~순천~속초를 잇는 동서고속화철도의 경우 2027년 개통을 앞두고 있습니다. KTX 속초역 개통과 동시에 향후 서울에서 속초까지 약 1시간대에 이동이 가능해질 전망입니다. 더불어 강릉선 KTX 이용객까지 더할 수 있어, 동해안 관광 유입도 기대할 수 있습니다.\n" +
                    "해외\n" +
                    "세컨하우스로 꼭 국내로만 고집할 필요는 없는데요. 멀지 않은 중국, 일본, 동남아 지역은 주말여행으로 충분히 다녀올 수 있기 때문입니다. 외국에서는 필리핀 세부&마닐라, 말레이시아 조호르바루 등이 꼽혔습니다.\n" +
                    "정말 다양한 지역이 있는데요. 이번 주말에는 자신에게 맞는 지역을 한 번 비교해 보며 찾아보는 것은 어떨까요?"+
                    "세컨하우스로 꼭 국내로만 고집할 필요는 없는데요. 멀지 않은 중국, 일본, 동남아 지역은 주말여행으로 충분히 다녀올 수 있기 때문입니다. 외국에서는 필리핀 세부&마닐라, 말레이시아 조호르바루 등이 꼽혔습니다.\n" +
                    "정말 다양한 지역이 있는데요. 이번 주말에는 자신에게 맞는 지역을 한 번 비교해 보며 찾아보는 것은 어떨까요? </div> </body>",
            imageUrls = mutableListOf("img-001", "img-002"),
            tmpYn = false
        )
    }
}