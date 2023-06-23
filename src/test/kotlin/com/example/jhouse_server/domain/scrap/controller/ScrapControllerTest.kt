package com.example.jhouse_server.domain.scrap.controller

import com.example.jhouse_server.domain.house.dto.HouseReqDto
import com.example.jhouse_server.domain.house.service.HouseService
import com.example.jhouse_server.domain.scrap.service.ScrapService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ScrapControllerTest @Autowired constructor(
    private val scrapService: ScrapService,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val houseService: HouseService,
): ApiControllerConfig("/api/v1/scraps") {
    private var accessToken: String? = null
    private var accessToken2 : String? = null
    private val userSignUpReqDto = MockEntity.testUserSignUpDto()
    private val userSignUpReqDto2 = MockEntity.testUserSignUpDto2()
    private val userSignInReqDto = MockEntity.testUserSignInDto()
    private val userSignInReqDto2 = MockEntity.testUserSignInDto2()
    private var user: User? = null
    private var anotherUser: User? = null
    private var houseId: Long? = null

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
        // create house
        houseId = houseService.createHouse(MockEntity.houseReqDto(), user!!)
    }

    @Test
    @DisplayName("빈집 게시글 스크랩")
    fun scrapHouse() {
        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        // then
        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "scrap-house",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("houseId").description("빈집 게시글 아이디")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data").description("빈집 게시글 아이")
                    )
                )
            )
    }

    @Test
    fun unScrapHouse() {
        // given
        scrapService.scrapHouse(houseId!!, anotherUser!!)
        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("$uri/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        // then
        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "unScrap-house",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("houseId").description("빈집 게시글 아이디")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("이미 스크랩한 경우")
    fun scrapHouse_already() {
        // given
        scrapService.scrapHouse(houseId!!, anotherUser!!)
        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{houseId}", houseId)
                .header(HttpHeaders.AUTHORIZATION, accessToken2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        // then
        resultActions
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "scrap-house-already",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("houseId").description("빈집 게시글 아이디")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("응답 메세지"),
                    )
                )
            )
    }
}