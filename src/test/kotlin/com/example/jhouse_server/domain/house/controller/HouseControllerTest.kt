package com.example.jhouse_server.domain.house.controller

import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.house.service.HouseService
import com.example.jhouse_server.domain.scrap.service.ScrapService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.MockEntity.Companion.houseReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.houseTmpReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.houseUpdateReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.reportReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.testUserSignInDto2
import com.example.jhouse_server.global.util.MockEntity.Companion.testUserSignUpDto2
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class HouseControllerTest @Autowired constructor(
    private val houseService: HouseService,
    private val houseRepository: HouseRepository,
    private val scrapService: ScrapService,
    private val userService: UserService,
    private val userRepository: UserRepository
): ApiControllerConfig("/api/v1/houses") {
    private var accessToken: String? = null
    private var accessToken2 : String? = null
    private val userSignUpReqDto = MockEntity.testUserSignUpDto()
    private val userSignUpReqDto2 = testUserSignUpDto2()
    private val userSignInReqDto = MockEntity.testUserSignInDto()
    private val userSignInReqDto2 = testUserSignInDto2()
    private var user: User? = null
    private var anotherUser: User? = null
    private var houseIds : MutableList<Long> = mutableListOf()

    @BeforeEach
    fun `로그인_더미데이터_생성`() {
        // singUp
        userService.signUp(userSignUpReqDto)
        user = userRepository.findByEmail(userSignUpReqDto.email).get()
        // signIn
        val tokenDto = userService.signIn(userSignInReqDto)
        accessToken = tokenDto.accessToken
        // another user signUp
        userService.signUp(userSignUpReqDto2)
        anotherUser = userRepository.findByEmail(userSignUpReqDto2.email).get()
        // another user signIn
        val tokenDto2 = userService.signIn(userSignInReqDto2)
        accessToken2 = tokenDto2.accessToken
    }
    @AfterEach
    fun `더미데이터_초기화`() {
        houseIds.clear()
    }

    @Test
    @DisplayName("빈집 게시글 생성")
    fun createHouse() {
        // given
        val req: String = objectMapper.writeValueAsString(MockEntity.houseReqDto())

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "save-house",
                    requestFields(
                        fieldWithPath("rentalType").description("매매 타입 ( SALE(매매), JEONSE(전세), MONTHLYRENT(월세) )은 필수값입니다. "),
                        fieldWithPath("city").description("주소는 '시도'로 시작하되, 줄이지 않은 표현으로 작성해야 합니다. 예: 서울시 서대문구 거북골로 34"),
                        fieldWithPath("zipCode").description("우편번호 ( string 형식입니다. )"),
                        fieldWithPath("size").description("집 크기는 m^2 단위로 산정해서 작성해야 합니다. string 형식입니다."),
                        fieldWithPath("purpose").description("매물 목적/용도 예: 거주 ( 방3, 화장실 2 )"),
                        fieldWithPath("floorNum").description("매물이 위치한 층수 ( 단독주택인 경우, null 로 전달 시 서버에서 0으로 세팅 )"),
                        fieldWithPath("contact").description("연락 가능한 연락처를 01000000000 형식으로 작성"),
                        fieldWithPath("createdDate").description("준공연도는 숫자만 입력해주세요. YYYY 형태로 서버에서 관리합니다."),
                        fieldWithPath("price").description("매물가격은 만원 단위로 산정해서 작성해주세요. ( 월세의 경우, 보증금을 작성해주세요. )"),
                        fieldWithPath("monthlyPrice").description("월세 가격은 만원 단위로 산정해서 소수점 포함으로 작성해주세요. Double 형식으로 관리합니다."),
                        fieldWithPath("agentName").description("공인중개사명을 작성해주세요. ( 없는 경우, 서버에서 null로 관리합니다. )"),
                        fieldWithPath("title").description("빈집 게시글의 제목을 입력해주세요."),
                        fieldWithPath("code").description("빈집 게시글의 내용을 입력해주세요."),
                        fieldWithPath("imageUrls").description("이미지 주소를 string 배열 형식으로 입력해주세요."),
                        fieldWithPath("tmpYn").description("임시저장 여부를 입력해주세요. (임시저장 : true, 저장: false) "),
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("빈집 게시글 아이디"),
                    )
                )
            )
    }

    @Test
    @DisplayName("빈집 게시글 수정")
    fun updateHouse() {
        // given
        val houseId = houseService.createHouse(MockEntity.houseReqDto(), user!!)
        val req: String = objectMapper.writeValueAsString(houseUpdateReqDto())

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "update-house",
                    pathParameters(
                        parameterWithName("houseId").description("빈집 게시글 아이디"),
                    ),
                    requestFields(
                        fieldWithPath("rentalType").description("매매 타입 ( SALE(매매), JEONSE(전세), MONTHLYRENT(월세) )은 필수값입니다. "),
                        fieldWithPath("city").description("주소는 '시도'로 시작하되, 줄이지 않은 표현으로 작성해야 합니다. 예: 서울시 서대문구 거북골로 34"),
                        fieldWithPath("zipCode").description("우편번호 ( string 형식입니다. )"),
                        fieldWithPath("size").description("집 크기는 m^2 단위로 산정해서 작성해야 합니다. string 형식입니다."),
                        fieldWithPath("purpose").description("매물 목적/용도 예: 거주 ( 방3, 화장실 2 )"),
                        fieldWithPath("floorNum").description("매물이 위치한 층수 ( 단독주택인 경우, null 로 전달 시 서버에서 0으로 세팅 )"),
                        fieldWithPath("contact").description("연락 가능한 연락처를 01000000000 형식으로 작성"),
                        fieldWithPath("createdDate").description("준공연도는 숫자만 입력해주세요. YYYY 형태로 서버에서 관리합니다."),
                        fieldWithPath("price").description("매물가격은 만원 단위로 산정해서 작성해주세요. ( 월세의 경우, 보증금을 작성해주세요. )"),
                        fieldWithPath("monthlyPrice").description("월세 가격은 만원 단위로 산정해서 소수점 포함으로 작성해주세요. Double 형식으로 관리합니다."),
                        fieldWithPath("agentName").description("공인중개사명을 작성해주세요. ( 없는 경우, 서버에서 null로 관리합니다. )"),
                        fieldWithPath("title").description("빈집 게시글의 제목을 입력해주세요."),
                        fieldWithPath("code").description("빈집 게시글의 내용을 입력해주세요."),
                        fieldWithPath("imageUrls").description("이미지 주소를 string 배열 형식으로 입력해주세요."),
                        fieldWithPath("tmpYn").description("임시저장 여부를 입력해주세요. (임시저장 : true, 저장: false) "),
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("빈집 게시글 아이디"),
                    )
                )
            )
    }

    @Test
    @DisplayName("빈집 게시글 삭제")
    fun deleteHouse() {
        // given
        val houseId = houseService.createHouse(houseReqDto(), user!!)

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("$uri/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document("delete-house",
                pathParameters(
                    parameterWithName("houseId").description("빈집 게시글 아이디")
                ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }
    @Test
    @DisplayName("빈집 게시글 목록 조회")
    fun getHouseAll() {
        // given
        // dummy for house ( with approve )
        for(i in 0..10) {
            val houseId = houseService.createHouse(houseReqDto(), user!!)
            houseRepository.findByIdOrThrow(houseId).approveEntity()
            houseIds.add(houseId)
            val houseId2 = houseService.createHouse(houseUpdateReqDto(), user!!)
            houseRepository.findByIdOrThrow(houseId2).approveEntity()
            houseIds.add(houseId2)
        }
        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri?rentalType=SALE&city=서울&search=")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "get-house-all",
                    requestParameters(
                        parameterWithName("rentalType").description("매매 타입"),
                        parameterWithName("city").description("지역 필터링"),
                        parameterWithName("search").description("검색어"),
//                        parameterWithName("page").description("페이지 번호 (0부터 !)"),
//                        parameterWithName("size").description("페이지 당 넘겨 받을 개수 ( 기본값 8개 )")
                    ),
                    responseFields(
//                        fieldWithPath("code").description("결과 코드"),
//                        fieldWithPath("message").description("응답 메세지"),
                        beneathPath("data").withSubsectionId("data"),
                        *pageResponseFieldSnippet()
                    )
                )
            )
    }

    @Test
    @DisplayName("빈집 게시글 단일 조회 - 비로그인")
    fun getHouseOne() {
        // given
        // dummy for house
        for(i in 0..10) {
            houseIds.add(houseService.createHouse(houseReqDto(), user!!))
            houseIds.add(houseService.createHouse(houseUpdateReqDto(), user!!))
        }

        val houseId = houseIds[0]

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/{houseId}", houseId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "get-house-one",
                    pathParameters(
                        parameterWithName("houseId").description("빈집 게시글 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.houseId").description("빈집 게시글 아이디"),
                        fieldWithPath("data.rentalType").description("매물 타입"),
                        fieldWithPath("data.city").description("매물 위치"),
                        fieldWithPath("data.zipcode").description("우편 주소"),
                        fieldWithPath("data.size").description("매물 크기 ( 견적 )"),
                        fieldWithPath("data.purpose").description("매물 목적/용도"),
                        fieldWithPath("data.floorNum").description("매물 층수"),
                        fieldWithPath("data.contact").description("연락 가능한 연락처"),
                        fieldWithPath("data.createdDate").description("준공연도"),
                        fieldWithPath("data.price").description("매물 가격"),
                        fieldWithPath("data.monthlyPrice").description("월세 가격"),
                        fieldWithPath("data.agentName").description("공인중개사명"),
                        fieldWithPath("data.title").description("게시글 제목"),
                        fieldWithPath("data.code").description("게시글 내용"),
                        fieldWithPath("data.imageUrls").description("게시글 이미지 주소"),
                        fieldWithPath("data.nickName").description("게시글 작성자"),
                        fieldWithPath("data.createdAt").description("게시글 작성일자"),
                        fieldWithPath("data.isCompleted").description("매물 거래 완료 여부"),
                        fieldWithPath("data.isScraped").description("빈집 게시글 스크랩 여부")
                    )
                )
            )
    }

    @Test
    @DisplayName("빈집 게시글 상세 조회 - 로그인")
    fun getHouseOneWithUser() {
        // dummy for house
        for(i in 0..10) {
            houseIds.add(houseService.createHouse(houseReqDto(), user!!))
            houseIds.add(houseService.createHouse(houseUpdateReqDto(), user!!))
        }

        val houseId = houseIds[0]

        scrapService.scrapHouse(houseId, user!!)

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/user-scrap/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "get-house-one-with-user",
                    pathParameters(
                        parameterWithName("houseId").description("빈집 게시글 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.houseId").description("빈집 게시글 아이디"),
                        fieldWithPath("data.rentalType").description("매물 타입"),
                        fieldWithPath("data.city").description("매물 위치"),
                        fieldWithPath("data.zipcode").description("우편 주소"),
                        fieldWithPath("data.size").description("매물 크기 ( 견적 )"),
                        fieldWithPath("data.purpose").description("매물 목적/용도"),
                        fieldWithPath("data.floorNum").description("매물 층수"),
                        fieldWithPath("data.contact").description("연락 가능한 연락처"),
                        fieldWithPath("data.createdDate").description("준공연도"),
                        fieldWithPath("data.price").description("매물 가격"),
                        fieldWithPath("data.monthlyPrice").description("월세 가격"),
                        fieldWithPath("data.agentName").description("공인중개사명"),
                        fieldWithPath("data.title").description("게시글 제목"),
                        fieldWithPath("data.code").description("게시글 내용"),
                        fieldWithPath("data.imageUrls").description("게시글 이미지 주소"),
                        fieldWithPath("data.nickName").description("게시글 작성자"),
                        fieldWithPath("data.createdAt").description("게시글 작성일자"),
                        fieldWithPath("data.isCompleted").description("매물 거래 완료 여부"),
                        fieldWithPath("data.isScraped").description("빈집 게시글 스크랩 여부 ( 로그인 상태일 때, 상세 조회 시 유저 데이터로부터 스크랩 여부를 판단합니다. )")
                    )
                )
            )
    }

    @Test
    @DisplayName("빈집 게시글 신고하기")
    fun reportHouse() {
        // given
        val houseId = houseService.createHouse(MockEntity.houseReqDto(), user!!)
        val req: String = objectMapper.writeValueAsString(reportReqDto())

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/report/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken2)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "report-house",
                    pathParameters(
                        parameterWithName("houseId").description("빈집 게시글 아이디"),
                    ),
                    requestFields(
                        fieldWithPath("reportReason").description("신고 이유는 필수값입니다."),
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                    )
                )
            )
    }

    @Test
    @DisplayName("임시저장된 게시글 목록 조회")
    fun getTmpSaveHouseAll() {
        // given
        // dummy for house ( with tmpSave )
        for(i in 0..10) {
            val houseId = houseService.createHouse(houseTmpReqDto(), user!!)
            houseIds.add(houseId)
        }
        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/tmp-save?page=0&size=8")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        // then
        resultActions
            .andExpect(status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "get-tmp-save-house-all",
                    requestParameters(
                        parameterWithName("page").description("페이지 번호 (0부터 !)"),
                        parameterWithName("size").description("페이지 당 넘겨 받을 개수 ( 기본값 8개 )")
                    ),
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *pageResponseFieldSnippet()
                    )
                )
            )
    }

    @Test
    @DisplayName("빈집 게시글 생성 - 길이 초과")
    fun createHouse_outOfLength() {
        // given
        val req: String = objectMapper.writeValueAsString(MockEntity.houseTooLongReqDto())

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(req)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        // then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                document(
                    "save-house-too-long",
                    requestFields(
                        fieldWithPath("rentalType").description("매매 타입 ( SALE(매매), JEONSE(전세), MONTHLYRENT(월세) )은 필수값입니다. "),
                        fieldWithPath("city").description("주소는 '시도'로 시작하되, 줄이지 않은 표현으로 작성해야 합니다. 예: 서울시 서대문구 거북골로 34"),
                        fieldWithPath("zipCode").description("우편번호 ( string 형식입니다. )"),
                        fieldWithPath("size").description("집 크기는 m^2 단위로 산정해서 작성해야 합니다. string 형식입니다."),
                        fieldWithPath("purpose").description("매물 목적/용도 예: 거주 ( 방3, 화장실 2 )"),
                        fieldWithPath("floorNum").description("매물이 위치한 층수 ( 단독주택인 경우, null 로 전달 시 서버에서 0으로 세팅 )"),
                        fieldWithPath("contact").description("연락 가능한 연락처를 01000000000 형식으로 작성"),
                        fieldWithPath("createdDate").description("준공연도는 숫자만 입력해주세요. YYYY 형태로 서버에서 관리합니다."),
                        fieldWithPath("price").description("매물가격은 만원 단위로 산정해서 작성해주세요. ( 월세의 경우, 보증금을 작성해주세요. )"),
                        fieldWithPath("monthlyPrice").description("월세 가격은 만원 단위로 산정해서 소수점 포함으로 작성해주세요. Double 형식으로 관리합니다."),
                        fieldWithPath("agentName").description("공인중개사명을 작성해주세요. ( 없는 경우, 서버에서 null로 관리합니다. )"),
                        fieldWithPath("title").description("빈집 게시글의 제목을 입력해주세요."),
                        fieldWithPath("code").description("빈집 게시글의 내용을 입력해주세요."),
                        fieldWithPath("imageUrls").description("이미지 주소를 string 배열 형식으로 입력해주세요."),
                        fieldWithPath("tmpYn").description("임시저장 여부를 입력해주세요. (임시저장 : true, 저장: false) "),
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                    )
                )
            )
    }

    private fun pageResponseFieldSnippet(): Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("content[].houseId").description("빈집 게시글 ID"),
            fieldWithPath("content[].rentalType").description("매매 타입"),
            fieldWithPath("content[].city").description("매물 위치"),
            fieldWithPath("content[].price").description("매물 가격"),
            fieldWithPath("content[].monthlyPrice").description("월세 가격"),
            fieldWithPath("content[].nickName").description("게시글 작성자"),
            fieldWithPath("content[].createdAt").description("게시글 작성날짜"),
            fieldWithPath("content[].isCompleted").description("거래 완료 여부"),
            fieldWithPath("content[].imageUrl").description("썸네일 이미지 주소"),
            fieldWithPath("pageable.sort.empty").description(""),
            fieldWithPath("pageable.sort.unsorted").description(""),
            fieldWithPath("pageable.sort.sorted").description(""),
            fieldWithPath("pageable.offset").description(""),
            fieldWithPath("pageable.pageNumber").description(""),
            fieldWithPath("pageable.pageSize").description(""),
            fieldWithPath("pageable.unpaged").description(""),
            fieldWithPath("pageable.paged").description(""),
            fieldWithPath("last").description(""),
            fieldWithPath("totalPages").description(""),
            fieldWithPath("totalElements").description(""),
            fieldWithPath("size").description(""),
            fieldWithPath("number").description(""),
            fieldWithPath("sort.empty").description(""),
            fieldWithPath("sort.unsorted").description(""),
            fieldWithPath("sort.sorted").description(""),
            fieldWithPath("first").description(""),
            fieldWithPath("numberOfElements").description(""),
            fieldWithPath("empty").description(""),
        )
    }
}