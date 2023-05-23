package com.example.jhouse_server.domain.record_comment.controller

import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.record_comment.service.RecordCommentService
import com.example.jhouse_server.domain.user.entity.AdminType
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
class RecordCommentControllerTest @Autowired constructor(
    private val recordCommentService: RecordCommentService,
    private val recordService: RecordService,
    private val userService: UserService,
    private val userRepository: UserRepository
): ApiControllerConfig("/api/v1/record_comment") {

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

        user = userRepository.findByEmail(userSignUpDto1.email).get()
        val user2 = userRepository.findByEmail(userSignUpDto2.email).get()
        user2.updateAuthority(Authority.ADMIN)
        user2.updateAdminType(AdminType.SERVER)

        val tokenDto = userService.signIn(userSignInDto1)
        accessToken = tokenDto.accessToken

        //record setting
        recordId = recordService.saveRecord(recordReqDto, user2)
    }

    @Test
    @DisplayName("댓글 저장")
    fun saveRecordComment() {
        //given
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)
        val content: String = objectMapper.writeValueAsString(recordCommentReqDto)

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
                    "save-record-comment",
                    requestFields(
                        fieldWithPath("record_id").description("레코드 아이디"),
                        fieldWithPath("parent_id").description("부모 댓글 아이디"),
                        fieldWithPath("content").description("댓글 내용")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("댓글 아이디")
                    )
                )
            )
    }

    @Test
    @DisplayName("댓글 수정")
    fun updateRecordComment() {
        //given
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user!!)
        val recordCommentUpdateDto = MockEntity.recordCommentUpdateDto()
        val content: String = objectMapper.writeValueAsString(recordCommentUpdateDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{comment_id}", commentId)
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
                    "update-record-comment",
                    pathParameters(
                        parameterWithName("comment_id").description("댓글 아이디")
                    ),
                    requestFields(
                        fieldWithPath("content").description("댓글 내용")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("댓글 아이디")
                    )
                )
            )
    }

    @Test
    @DisplayName("댓글 삭제")
    fun deleteRecordComment() {
        //given
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("$uri/{comment_id}", commentId)
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
                    "delete-record-comment",
                    pathParameters(
                        parameterWithName("comment_id").description("댓글 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }
}