package com.example.jhouse_server.domain.user.controller

import com.example.jhouse_server.domain.user.CheckSmsReqDto
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.RedisUtil
import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension::class)
internal class UserControllerTest @Autowired constructor(
        private var mockMvc: MockMvc,
        private val objectMapper: ObjectMapper,
        private val userService: UserService,
        private val redisUtil: RedisUtil
) {

    private val userSignUpDto = MockEntity.testUserSignUpDto()

    private val userSignInDto = MockEntity.testUserSignInDto()

    @BeforeEach
    fun setUp(webApplicationContext: WebApplicationContext,
              restDocumentationContextProvider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
                .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint()))
                .build()
    }

    @Test
    @DisplayName("이메일 중복 검사")
    fun emailCheck() {
        //given

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/api/v1/users/check/email/{email}", "user@jhouse.com")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "email-check",
                                pathParameters(
                                        parameterWithName("email").description("이메일")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data").description("사용 가능 여부")
                                )
                        )
                )
    }

    @Test
    @DisplayName("닉네임 중복 검사")
    fun nickNameCheck() {
        //given

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("/api/v1/users/check/nick-name/{nick-name}", "zzangu")
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "nickname-check",
                                pathParameters(
                                        parameterWithName("nick-name").description("닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data").description("사용 가능 여부")
                                )
                        )
                )
    }

//    @Test
//    @DisplayName("인증문자 전송 테스트")
//    fun sendSms() {
//        //given
//
//        //when
//        val resultActions = mockMvc.perform(
//                RestDocumentationRequestBuilders
//                        .post("/api/v1/users/send/sms")
//                        .param("phone_num", "01011111111")
//                        .accept(APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//        )
//
//        //then
//        resultActions
//                .andExpect(status().isOk)
//                .andDo(print())
//                .andDo(
//                        document(
//                                "send-sms",
//                                requestParameters(
//                                        parameterWithName("phone_num").description("전화번호")
//                                ),
//                                responseFields(
//                                        fieldWithPath("code").description("결과 코드"),
//                                        fieldWithPath("message").description("응답 메세지")
//                                )
//                        )
//                )
//    }

//    @Test
//    @DisplayName("인증문자 검증 테스트")
//    fun checkSms() {
//        //given
//        val phoneNum = "01011111111"
//        userService.sendSmsCode(phoneNum)
//        val code = redisUtil.getValues(phoneNum).toString()
//        val checkSmsReqDto = CheckSmsReqDto(phoneNum, code)
//
//        val content: String = objectMapper.writeValueAsString(checkSmsReqDto)
//
//
//        //when
//        val resultActions = mockMvc.perform(
//                RestDocumentationRequestBuilders
//                        .post("/api/v1/users/check/sms")
//                        .content(content)
//                        .contentType(APPLICATION_JSON)
//                        .accept(APPLICATION_JSON)
//                        .characterEncoding("UTF-8")
//        )
//
//        //then
//        resultActions
//                .andExpect(status().isOk)
//                .andDo(print())
//                .andDo(
//                        document(
//                                "check-sms",
//                                requestFields(
//                                        fieldWithPath("phone_num").description("전화번호"),
//                                        fieldWithPath("code").description("인증 코드")
//                                ),
//                                responseFields(
//                                        fieldWithPath("code").description("결과 코드"),
//                                        fieldWithPath("message").description("응답 메세지"),
//                                        fieldWithPath("data").description("검증 결과")
//                                )
//                        )
//                )
//    }

    @Test
    @DisplayName("회원가입 테스트")
    fun signUp() {
        //given
        val content: String = objectMapper.writeValueAsString(userSignUpDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/api/v1/users/sign-up")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "sign-up",
                                requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호"),
                                        fieldWithPath("nick_name").description("닉네임"),
                                        fieldWithPath("phone_num").description("전화번호")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                )
    }

    @Test
    @DisplayName("로그인 테스트")
    fun signIn() {
        //given
        userService.signUp(userSignUpDto)
        val content: String = objectMapper.writeValueAsString(userSignInDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/api/v1/users/sign-in")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "sign-in",
                                requestFields(
                                        fieldWithPath("email").description("이메일"),
                                        fieldWithPath("password").description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data.access_token").description("엑세스 토큰"),
                                        fieldWithPath("data.refresh_token").description("리프레쉬 토큰")
                                )
                        )
                )
    }

    @Test
    @DisplayName("토큰 재발급 테스트")
    fun reissue() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val content: String = objectMapper.writeValueAsString(tokenDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/api/v1/users/reissue")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "reissue",
                                requestFields(
                                        fieldWithPath("access_token").description("액세스 토큰"),
                                        fieldWithPath("refresh_token").description("리프레쉬 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data.access_token").description("엑세스 토큰"),
                                        fieldWithPath("data.refresh_token").description("리프레쉬 토큰")
                                )
                        )
                )
    }

    @Test
    @DisplayName("로그아웃 테스트")
    fun logout() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("/api/v1/users/logout")
                        .header(AUTHORIZATION, accessToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "logout",
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                )

        assertThat(redisUtil.getValues(userSignUpDto.email)).isNull()
    }

    @Test
    @DisplayName("닉네임 수정 테스트")
    fun updateNickName() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .put("/api/v1/users/update/nick-name/{nick-name}", "zzangu")
                        .header(AUTHORIZATION, accessToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "update-nickname",
                                pathParameters(
                                        parameterWithName("nick-name").description("닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                )
    }

    @Test
    @DisplayName("비밀번호 수정 테스트")
    fun updatePassword() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .put("/api/v1/users/update/password/{password}", "jhouse123!")
                        .header(AUTHORIZATION, accessToken)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isOk)
                .andDo(print())
                .andDo(
                        document(
                                "update-password",
                                pathParameters(
                                        parameterWithName("password").description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                )
    }
}
