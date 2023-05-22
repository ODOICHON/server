package com.example.jhouse_server.domain.record.controller

import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.repository.RecordRepository
import com.example.jhouse_server.domain.record.service.RecordService
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecordControllerTest @Autowired constructor(
    private val recordService: RecordService,
    private val recordRepository: RecordRepository,
    private val userService: UserService,
    private val userRepository: UserRepository
): ApiControllerConfig("/api/v1/record") {

    private val userSignUpDto1 = MockEntity.testUserSignUpDto()
    private val userSignUpDto2 = MockEntity.testUserSignUpDto2()
    private val userSignInDto1 = MockEntity.testUserSignInDto()

    private val odoriReqDto = MockEntity.odoriReqDto()
    private val retroReqDto = MockEntity.retroReqDto()
    private val techReqDtoNewTech = MockEntity.techReqDtoNewTech()
    private val techReqDtoIssue = MockEntity.techReqDtoIssue()
    private val recordUpdateDto = MockEntity.recordUpdateDto()

    private val recordIds: MutableList<Long> = mutableListOf()
    private var accessToken: String? = null
    private var user: User? = null

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto1)
        userService.signUp(userSignUpDto2)

        user = userRepository.findByEmail(userSignUpDto1.email).get()
        val user2 = userRepository.findByEmail(userSignUpDto2.email).get()
        user!!.updateAuthority(Authority.ADMIN)
        user!!.updateAdminType(AdminType.SERVER)
        user2.updateAuthority(Authority.ADMIN)
        user2.updateAdminType(AdminType.SERVER)

        val tokenDto = userService.signIn(userSignInDto1)
        accessToken = tokenDto.accessToken

        //record setting
        val recordId1 = recordService.saveRecord(odoriReqDto, user2)
        val recordId2 = recordService.saveRecord(retroReqDto, user2)
        val recordId3 = recordService.saveRecord(techReqDtoNewTech, user2)
        val recordId4 = recordService.saveRecord(techReqDtoNewTech, user2)
        val recordId5 = recordService.saveRecord(techReqDtoIssue, user2)
        val record1 = recordRepository.findById(recordId1).get()
        val record2 = recordRepository.findById(recordId2).get()
        val record3 = recordRepository.findById(recordId3).get()
        val record4 = recordRepository.findById(recordId4).get()
        val record5 = recordRepository.findById(recordId5).get()
        record1.updateRecordStatus(RecordStatus.APPROVE)
        record2.updateRecordStatus(RecordStatus.APPROVE)
        record3.updateRecordStatus(RecordStatus.APPROVE)
        record4.updateRecordStatus(RecordStatus.APPROVE)
        record5.updateRecordStatus(RecordStatus.APPROVE)

        recordIds.add(recordId1)
        recordIds.add(recordId2)
        recordIds.add(recordId3)
        recordIds.add(recordId4)
        recordIds.add(recordId5)
    }

    @Test
    @DisplayName("레코드 저장")
    fun saveRecord() {
        //given
        val content: String = objectMapper.writeValueAsString(odoriReqDto)

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
                    "save-record",
                    requestFields(
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("content").description("내용"),
                        fieldWithPath("part").description("파트"),
                        fieldWithPath("category").description("하위 카테고리"),
                        fieldWithPath("type").description("카테고리")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("레코드 아이디")
                    )
                )
            )
    }
}