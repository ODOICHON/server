package com.example.jhouse_server.domain.record_category.controller

import com.example.jhouse_server.domain.record_category.service.RecordCategoryService
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
class RecordCategoryControllerTest @Autowired constructor(
    private val recordCategoryService: RecordCategoryService,
    private val userService: UserService,
    private val userRepository: UserRepository
): ApiControllerConfig("/api/v1/record_category") {

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val userSignInDto = MockEntity.testUserSignInDto()
    private val templateSaveReqDto = MockEntity.templateSaveReqDto()

    private var accessToken: String? = null
    private var user: User? = null

    @BeforeEach
    fun before() {
        userService.signUp(userSignUpDto)

        user = userRepository.findByEmail(userSignUpDto.email).get()
        user!!.updateAuthority(Authority.ADMIN)
        user!!.updateAdminType(AdminType.SERVER)

        val tokenDto = userService.signIn(userSignInDto)
        accessToken = tokenDto.accessToken
    }

    @Test
    @DisplayName("템플릿 저장 & 수정")
    fun updateTemplateTest() {
        //given
        val content: String = objectMapper.writeValueAsString(templateSaveReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/template")
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
                    "update-template",
                    requestFields(
                        fieldWithPath("category").description("하위 카테고리"),
                        fieldWithPath("template").description("템플릿 내용")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("템플릿 조회")
    fun getTemplateTest() {
        //given
        recordCategoryService.updateTemplate(templateSaveReqDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/template/{category}", "culture")
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
                    "get-template",
                    pathParameters(
                        parameterWithName("category").description("하위 카테고리")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.category").description("하위 카테고리"),
                        fieldWithPath("data.template").description("템플릿 내용")
                    )
                )
            )
    }
}