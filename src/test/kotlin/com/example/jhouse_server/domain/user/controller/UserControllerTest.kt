package com.example.jhouse_server.domain.user.controller

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.RedisUtil
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional
import javax.servlet.http.Cookie

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserControllerTest @Autowired constructor(
        private val userRepository: UserRepository,
        private val userService: UserService,
        private val redisUtil: RedisUtil,
        private val tokenProvider: TokenProvider
): ApiControllerConfig("/api/v1/users") {

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val userSignInDto = MockEntity.testUserSignInDto()
    private val emailReqDto = MockEntity.testEmailDto()
    private val phoneNumReqDto = MockEntity.testPhoneNumDto()
    private val nickNameReqDto = MockEntity.testNickNameDto()
    private val passwordReqDto = MockEntity.testPasswordDto()

    @Test
    @DisplayName("유저 정보 조회")
    fun getUser() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .get("$uri")
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
                                "get-user",
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data.id").description("유저 DB 아이디"),
                                        fieldWithPath("data.email").description("아이디"),
                                        fieldWithPath("data.nick_name").description("닉네임"),
                                        fieldWithPath("data.phone_num").description("전화번호"),
                                        fieldWithPath("data.authority").description("권한"),
                                        fieldWithPath("data.age").description("연령대"),
                                        fieldWithPath("data.profile_image_url").description("프로필 이미지 URL"),
                                        fieldWithPath("data.userType").description("사용자 타입 ( 일반 사용자 : NONE, 공인중개사 : AGENT, 관리자 : WEB/SERVER )"),
                                )
                        )
                )
    }

    @Test
    @DisplayName("이메일 중복 검사")
    fun emailCheck() {
        //given
        val content: String = objectMapper.writeValueAsString(emailReqDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/check/email")
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
                                "email-check",
                                requestFields(
                                        fieldWithPath("email").description("이메일")
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
        val content: String = objectMapper.writeValueAsString(nickNameReqDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/check/nick-name")
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
                                "nickname-check",
                                requestFields(
                                        fieldWithPath("nick_name").description("닉네임")
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
    @DisplayName("인증문자 전송 테스트")
    fun sendSms() {
        //given
        val content: String = objectMapper.writeValueAsString(phoneNumReqDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/send/sms")
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
                                "send-sms",
                                requestFields(
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
    @DisplayName("인증문자 전송 테스트 - 예외처리")
    fun sendSmsException() {
        //given
        userService.signUp(userSignUpDto)
        val content: String = objectMapper.writeValueAsString(phoneNumReqDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/send/sms")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        resultActions
                .andExpect(status().isBadRequest)
                .andDo(print())
                .andDo(
                        document(
                                "send-sms-exception",
                                requestFields(
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
    @DisplayName("인증문자 검증 테스트")
    fun checkSms() {
        //given
        val phoneNum = "01011111111"
        userService.sendSmsCode(phoneNum)
        val code = redisUtil.getValues(phoneNum).toString()
        val checkSmsReqDto = CheckSmsReqDto(phoneNum, code)

        val content: String = objectMapper.writeValueAsString(checkSmsReqDto)


        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/check/sms")
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
                                "check-sms",
                                requestFields(
                                        fieldWithPath("phone_num").description("전화번호"),
                                        fieldWithPath("code").description("인증 코드")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data").description("검증 결과")
                                )
                        )
                )
    }

    @Test
    @DisplayName("회원가입 테스트")
    fun signUp() {
        //given
        val content: String = objectMapper.writeValueAsString(userSignUpDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/sign-up")
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
                                        fieldWithPath("phone_num").description("전화번호"),
                                        fieldWithPath("age").description("연령대"),
                                        fieldWithPath("join_paths").description("가입 경로"),
                                        fieldWithPath("terms").description("약관 동의 내역 ( enum 변수명으로 보내야 합니다. )")
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
                        .post("$uri/sign-in")
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
                                        fieldWithPath("data.access_token").description("엑세스 토큰")
//                                        fieldWithPath("data.refresh_token").description("리프레쉬 토큰")
                                )
                        )
                )
    }

    @Test
    @DisplayName("로그인 테스트 - 예외처리")
    fun signInException() {
        //given
        userService.signUp(userSignUpDto)
        val content1: String = objectMapper.writeValueAsString(MockEntity.testUserSignInDtoEx1())
        val content2: String = objectMapper.writeValueAsString(MockEntity.testUserSignInDtoEx2())

        //when
        val resultActions1 = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/sign-in")
                .content(content1)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        val resultActions2 = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/sign-in")
                .content(content2)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions1
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "sign-in-exception-1",
                    requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
        resultActions2
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "sign-in-exception-2",
                    requestFields(
                        fieldWithPath("email").description("이메일"),
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
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
//        val refreshToken = tokenProvider.createRefreshToken()
        val cookie = Cookie("RefreshToken", tokenProvider.resolveToken(tokenDto.accessToken))
        cookie.domain = "localhost"
        cookie.maxAge = 60 * 60 * 24 * 7
        cookie.path = "/"
        cookie.isHttpOnly = true
        cookie.secure = true

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/reissue")
                        .content(content)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .cookie(cookie)
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
                                        fieldWithPath("access_token").description("액세스 토큰")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지"),
                                        fieldWithPath("data.access_token").description("엑세스 토큰")
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
                        .post("$uri/logout")
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
    }

    @Test
    @DisplayName("닉네임 수정 테스트")
    fun updateNickName() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken
        val content: String = objectMapper.writeValueAsString(nickNameReqDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .put("$uri/update/nick-name")
                        .header(AUTHORIZATION, accessToken)
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
                                "update-nickname",
                                requestFields(
                                        fieldWithPath("nick_name").description("닉네임")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                )
    }

    @Test
    @DisplayName("닉네임 수정 테스트 - 예외처리")
    fun updateNickNameException() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken
        val content: String = objectMapper.writeValueAsString(NickNameReqDto(userSignUpDto.nickName))

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/update/nick-name")
                .header(AUTHORIZATION, accessToken)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "update-nickname-exception",
                    requestFields(
                        fieldWithPath("nick_name").description("닉네임")
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
        val content: String = objectMapper.writeValueAsString(passwordReqDto)

        //when
        val resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .put("$uri/update/password")
                        .header(AUTHORIZATION, accessToken)
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
                                "update-password",
                                requestFields(
                                        fieldWithPath("password").description("비밀번호")
                                ),
                                responseFields(
                                        fieldWithPath("code").description("결과 코드"),
                                        fieldWithPath("message").description("응답 메세지")
                                )
                        )
                )
    }

    @Test
    @DisplayName("비밀번호 수정 테스트 - 예외처리")
    fun updatePasswordException() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken
        val content: String = objectMapper.writeValueAsString(PasswordReqDto(userSignUpDto.password))

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/update/password")
                .header(AUTHORIZATION, accessToken)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "update-password-exception",
                    requestFields(
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("회원정보 수정 테스트")
    fun update() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken
        val userUpdateReqDto = MockEntity.userUpdateReqDto("abcdefg12345!")
        val content: String = objectMapper.writeValueAsString(userUpdateReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri")
                .header(AUTHORIZATION, accessToken)
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
                    "update-user",
                    requestFields(
                        fieldWithPath("password").description("비밀번호"),
                        fieldWithPath("nick_name").description("닉네임"),
                        fieldWithPath("new_password").description("변경할 비밀번호"),
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
    @DisplayName("사용자 탈퇴 테스트")
    fun withdrawal() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken
        val withdrawalUserReqDto = MockEntity.withdrawalUserReqDto("content!")
        val content: String = objectMapper.writeValueAsString(withdrawalUserReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/withdrawal")
                .header(AUTHORIZATION, accessToken)
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
                    "withdrawal",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("사용자 탈퇴 테스트 - 예외처리")
    fun withdrawalException() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken
        val withdrawalUserReqDto = MockEntity.withdrawalUserReqDto("content!")
        val content: String = objectMapper.writeValueAsString(withdrawalUserReqDto)
        val user = userRepository.findByEmail(userSignUpDto.email).get()
        userService.withdrawal(user, withdrawalUserReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/withdrawal")
                .header(AUTHORIZATION, accessToken)
                .content(content)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "withdrawal-exception",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    //JWT 관련
    //정규식
    @Test
    @DisplayName("정규식 예외처리")
    fun regularExpression() {
        //given
        userService.signUp(userSignUpDto)
        val tokenDto = userService.signIn(userSignInDto)
        val accessToken = tokenDto.accessToken

        val emailRE = EmailReqDto("123")
        val passwordRE = PasswordReqDto("a123")
        val phoneNumRE = PhoneNumReqDto("010111")
        val nickNameRE = NickNameReqDto("!@#$%^")
        val codeRE = CheckSmsReqDto("01011111111", "123")

        val emailContent: String = objectMapper.writeValueAsString(emailRE)
        val passwordContent: String = objectMapper.writeValueAsString(passwordRE)
        val phoneNumContent: String = objectMapper.writeValueAsString(phoneNumRE)
        val nickNameContent: String = objectMapper.writeValueAsString(nickNameRE)
        val codeContent: String = objectMapper.writeValueAsString(codeRE)

        //when
        val emailResultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/check/email")
                .content(emailContent)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        val passwordResultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/update/password")
                .header(AUTHORIZATION, accessToken)
                .content(passwordContent)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        val phoneNumResultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/send/sms")
                        .content(phoneNumContent)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )
        val nickNameResultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/check/nick-name")
                .content(nickNameContent)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        val codeResultActions = mockMvc.perform(
                RestDocumentationRequestBuilders
                        .post("$uri/check/sms")
                        .content(codeContent)
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        )

        //then
        emailResultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "email-validation",
                    requestFields(
                        fieldWithPath("email").description("이메일")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
        passwordResultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "password-validation",
                    requestFields(
                        fieldWithPath("password").description("비밀번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
        phoneNumResultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "phonenum-validation",
                    requestFields(
                        fieldWithPath("phone_num").description("전화번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
        nickNameResultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "nickname-validation",
                    requestFields(
                        fieldWithPath("nick_name").description("닉네임")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
        codeResultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "code-validation",
                    requestFields(
                        fieldWithPath("phone_num").description("전화번호"),
                        fieldWithPath("code").description("인증 코드")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }
}
