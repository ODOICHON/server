package com.example.jhouse_server.domain.user.controller

import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class AgentControllerTest: ApiControllerConfig("/api/v1/agents") {

    private val agentSignUpDto = MockEntity.testAgentSignUpDto()

    @Test
    @DisplayName("공인중개사 회원가입 테스트")
    fun signUp() {
        //given
        val content: String = objectMapper.writeValueAsString(agentSignUpDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("$uri/sign-up")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "agent-sign-up",
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("email").description("이메일"),
                        PayloadDocumentation.fieldWithPath("password").description("비밀번호"),
                        PayloadDocumentation.fieldWithPath("nick_name").description("닉네임"),
                        PayloadDocumentation.fieldWithPath("phone_num").description("전화번호"),
                        PayloadDocumentation.fieldWithPath("age").description("연령대"),
                        PayloadDocumentation.fieldWithPath("join_paths").description("가입 경로"),
                        PayloadDocumentation.fieldWithPath("agent_code").description("공인중개사 등록번호"),
                        PayloadDocumentation.fieldWithPath("business_code").description("사업자 등록번호"),
                        PayloadDocumentation.fieldWithPath("company_name").description("공인중개사 사무소 상호명"),
                        PayloadDocumentation.fieldWithPath("agent_name").description("대표자 이름"),
                        PayloadDocumentation.fieldWithPath("company_phone_num").description("공인중개사 사무소 대표 전화번호"),
                        PayloadDocumentation.fieldWithPath("assistant_name").description("중개 보조원명"),
                        PayloadDocumentation.fieldWithPath("company_address").description("공인중개사 사무소 주소"),
                        PayloadDocumentation.fieldWithPath("company_address_detail").description("공인중개사 사무소 상세 주소"),
                        PayloadDocumentation.fieldWithPath("company_email").description("공인중개사 이메일"),
                        PayloadDocumentation.fieldWithPath("estate").description("주거래 매물")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }
}