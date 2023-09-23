package com.example.jhouse_server.domain.record_review.controller

import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.record_review.service.RecordReviewService
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecordReviewControllerTest @Autowired constructor(
    private val recordReviewService: RecordReviewService,
    private val recordService: RecordService,
    private val userService: UserService,
    private val userRepository: UserRepository
): ApiControllerConfig("/api/v1/record_review") {

    private val userSignUpDto1 = MockEntity.testUserSignUpDto()
    private val userSignUpDto2 = MockEntity.testUserSignUpDto2()
    private val userSignInDto1 = MockEntity.testUserSignInDto()
    private val recordReqDto = MockEntity.odoriReqDto()

    private var accessToken: String? = null
    private var recordId: Long? = null
    private var user: User? = null

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto1)
        userService.signUp(userSignUpDto2)

        user = userRepository.findByUserName(userSignUpDto1.userName).get()
        val user2 = userRepository.findByUserName(userSignUpDto2.userName).get()
        user!!.updateAuthority(Authority.ADMIN)
        user!!.updateUserType(UserType.SERVER)
        user2.updateAuthority(Authority.ADMIN)
        user2.updateUserType(UserType.SERVER)

        val tokenDto = userService.signIn(userSignInDto1)
        accessToken = tokenDto.accessToken

        //record setting
        recordId = recordService.saveRecord(recordReqDto, user2)
    }

    @Test
    @DisplayName("리뷰 저장")
    fun saveRecordReview() {
        //given
        val recordReviewReqDto = MockEntity.recordReviewReqDto(recordId!!, "approve")
        val content: String = objectMapper.writeValueAsString(recordReviewReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "save-record-review",
                    requestFields(
                        fieldWithPath("record_id").description("레코드 아이디"),
                        fieldWithPath("content").description("리뷰 내용"),
                        fieldWithPath("status").description("리뷰 상태")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("리뷰 아이디")
                    )
                )
            )
    }

    @Test
    @DisplayName("리뷰 수정")
    fun updateRecordReview() {
        //given
        val recordReviewReqDto = MockEntity.recordReviewReqDto(recordId!!, "reject")
        val reviewId = recordReviewService.saveRecordReview(recordReviewReqDto, user!!)
        val recordReviewUpdateDto = MockEntity.recordReviewUpdateDto()
        val content: String = objectMapper.writeValueAsString(recordReviewUpdateDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{review_id}", reviewId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "update-record-review",
                    pathParameters(
                        parameterWithName("review_id").description("리뷰 아이디")
                    ),
                    requestFields(
                        fieldWithPath("content").description("리뷰 내용"),
                        fieldWithPath("status").description("리뷰 상태")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("리뷰 아이디")
                    )
                )
            )
    }

    @Test
    @DisplayName("리뷰 삭제")
    fun deleteRecordReview() {
        //given
        val recordReviewReqDto = MockEntity.recordReviewReqDto(recordId!!, "reject")
        val reviewId = recordReviewService.saveRecordReview(recordReviewReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("$uri/{review_id}", reviewId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "delete-record-review",
                    pathParameters(
                        parameterWithName("review_id").description("리뷰 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    /**
     * Exception
     */

    @Test
    @DisplayName("리뷰 저장 예외처리")
    fun saveRecordReviewException() {
        //given
        val recordReviewReqDto = MockEntity.recordReviewReqDto(recordId!!, "approve")
        recordReviewService.saveRecordReview(recordReviewReqDto, user!!)
        val content: String = objectMapper.writeValueAsString(recordReviewReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "save-record-review-exception",
                    requestFields(
                        fieldWithPath("record_id").description("레코드 아이디"),
                        fieldWithPath("content").description("리뷰 내용"),
                        fieldWithPath("status").description("리뷰 상태")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("리뷰 수정 예외처리")
    fun updateRecordReviewException() {
        //given
        val recordReviewReqDto = MockEntity.recordReviewReqDto(recordId!!, "approve")
        val reviewId = recordReviewService.saveRecordReview(recordReviewReqDto, user!!)
        val recordReviewUpdateDto = MockEntity.recordReviewUpdateDto()
        val content: String = objectMapper.writeValueAsString(recordReviewUpdateDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{review_id}", reviewId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "update-record-review-exception",
                    pathParameters(
                        parameterWithName("review_id").description("리뷰 아이디")
                    ),
                    requestFields(
                        fieldWithPath("content").description("리뷰 내용"),
                        fieldWithPath("status").description("리뷰 상태")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("리뷰 삭제 예외처리")
    fun deleteRecordReviewException() {
        //given
        val recordReviewReqDto = MockEntity.recordReviewReqDto(recordId!!, "approve")
        val reviewId = recordReviewService.saveRecordReview(recordReviewReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("$uri/{review_id}", reviewId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isBadRequest)
            .andDo(print())
            .andDo(
                document(
                    "delete-record-review-exception",
                    pathParameters(
                        parameterWithName("review_id").description("리뷰 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }
}