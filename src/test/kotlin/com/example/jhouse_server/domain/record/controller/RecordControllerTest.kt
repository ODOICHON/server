package com.example.jhouse_server.domain.record.controller

import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.record.repository.RecordRepository
import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.record_comment.service.RecordCommentService
import com.example.jhouse_server.domain.record_review.service.RecordReviewService
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import org.junit.jupiter.api.AfterEach
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecordControllerTest @Autowired constructor(
    private val recordService: RecordService,
    private val recordRepository: RecordRepository,
    private val recordCommentService: RecordCommentService,
    private val recordReviewService: RecordReviewService,
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
        user!!.updateUserType(UserType.SERVER)
        user2.updateAuthority(Authority.ADMIN)
        user2.updateUserType(UserType.SERVER)

        val tokenDto = userService.signIn(userSignInDto1)
        accessToken = tokenDto.accessToken

        //record setting
        val recordId1 = recordService.saveRecord(odoriReqDto, user2)
        val recordId2 = recordService.saveRecord(retroReqDto, user2)
        val recordId3 = recordService.saveRecord(techReqDtoNewTech, user2)
        val recordId4 = recordService.saveRecord(techReqDtoNewTech, user2)
        val recordId5 = recordService.saveRecord(techReqDtoIssue, user2)

        recordIds.add(recordId1)
        recordIds.add(recordId2)
        recordIds.add(recordId3)
        recordIds.add(recordId4)
        recordIds.add(recordId5)
    }

    @AfterEach
    fun after() {
        recordIds.clear()
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

    @Test
    @DisplayName("레코드 수정")
    fun updateRecord() {
        //given
        val recordId = recordService.saveRecord(odoriReqDto, user!!)
        val content: String = objectMapper.writeValueAsString(recordUpdateDto)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{record_id}", recordId)
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
                    "update-record",
                    pathParameters(
                        parameterWithName("record_id").description("레코드 아이디")
                    ),
                    requestFields(
                        fieldWithPath("title").description("제목"),
                        fieldWithPath("content").description("내용")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("레코드 아이디")
                    )
                )
            )
    }

    @Test
    @DisplayName("레코드 삭제")
    fun deleteRecord() {
        //given
        val recordId = recordService.saveRecord(odoriReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete("$uri/{record_id}", recordId)
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
                    "delete-record",
                    pathParameters(
                        parameterWithName("record_id").description("레코드 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지")
                    )
                )
            )
    }

    @Test
    @DisplayName("핫 레코드 목록 조회")
    fun getHotRecords() {
        //given
        val recordId1 = recordIds[0]
        val recordId2 = recordIds[1]
        val recordId3 = recordIds[2]
        val recordId4 = recordIds[3]
        val recordId5 = recordIds[4]
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
        record1.updateHits()
        record2.updateHits()
        record3.updateHits()

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/hot")
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
                    "get-hot-records",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.records[].record_id").description("레코드 아이디"),
                        fieldWithPath("data.records[].title").description("제목")
                    )
                )
            )
    }

    @Test
    @DisplayName("레코드 목록 조회")
    fun getRecords() {
        //given
        val recordId1 = recordIds[0]
        val recordId2 = recordIds[1]
        val recordId3 = recordIds[2]
        val recordId4 = recordIds[3]
        val recordId5 = recordIds[4]
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

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/{part}/{type}?category=new_tech&page=0", "server", "tech")
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
                    "get-records",
                    pathParameters(
                        parameterWithName("part").description("파트"),
                        parameterWithName("type").description("카테고리")
                    ),
                    requestParameters(
                        parameterWithName("category").description("하위 카테고리"),
                        parameterWithName("page").description("페이지 번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.records.content[].record_id").description("레코드 아이디"),
                        fieldWithPath("data.records.content[].title").description("제목"),
                        fieldWithPath("data.records.content[].content").description("내용"),
                        fieldWithPath("data.records.content[].nick_name").description("닉네임"),
                        fieldWithPath("data.records.content[].create_at").description("생성 일자"),
                        fieldWithPath("data.records.content[].part").description("파트"),

                        *pageFieldWithPaths("data.records")
                    )
                )
            )
    }

    @Test
    @DisplayName("레코드 조회")
    fun getRecord() {
        //given
        val recordId = recordIds[0]
        val record = recordRepository.findById(recordId).get()
        record.updateRecordStatus(RecordStatus.APPROVE)

        var recordCommentReqDto = MockEntity.recordCommentReqDto(recordId, null)
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user!!)
        recordCommentService.saveRecordComment(recordCommentReqDto, user!!)
        recordCommentReqDto = MockEntity.recordCommentReqDto(recordId, commentId)
        recordCommentService.saveRecordComment(recordCommentReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/{record_id}?page=0", recordId)
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
                    "get-record",
                    pathParameters(
                        parameterWithName("record_id").description("레코드 아이디")
                    ),
                    requestParameters(
                        parameterWithName("page").description("댓글 페이지 번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.record_id").description("레코드 아이디"),
                        fieldWithPath("data.title").description("제목"),
                        fieldWithPath("data.content").description("내용"),
                        fieldWithPath("data.hits").description("조회수"),
                        fieldWithPath("data.part").description("파트"),
                        fieldWithPath("data.type").description("카테고리"),
                        fieldWithPath("data.category").description("하위 카테고리"),
                        fieldWithPath("data.nick_name").description("닉네임"),
                        fieldWithPath("data.create_at").description("생성 일자"),
                        fieldWithPath("data.comments.content[].comment_id").description("댓글 아이디"),
                        fieldWithPath("data.comments.content[].level").description("댓글 레벨"),
                        fieldWithPath("data.comments.content[].content").description("댓글 내용"),
                        fieldWithPath("data.comments.content[].nick_name").description("댓글 작성자 닉네임"),
                        fieldWithPath("data.comments.content[].create_at").description("댓글 생성 일자"),

                        *pageFieldWithPaths("data.comments")
                    )
                )
            )
    }

    @Test
    @DisplayName("레코드 조회 - 리뷰")
    fun getRecordWithReview() {
        //given
        val recordId = recordIds[0]
        val user2 = userRepository.findByEmail(userSignUpDto2.email).get()

        var recordReviewReqDto = MockEntity.recordReviewReqDtoAll(recordId, "이 부분 수정해주세요.", "reject")
        recordReviewService.saveRecordReview(recordReviewReqDto, user!!)
        recordReviewReqDto = MockEntity.recordReviewReqDtoAll(recordId, "수정했습니다.", "mine")
        recordReviewService.saveRecordReview(recordReviewReqDto, user2)
        recordReviewReqDto = MockEntity.recordReviewReqDtoAll(recordId, "확인했습니다!", "approve")
        recordReviewService.saveRecordReview(recordReviewReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/review/{record_id}", recordId)
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
                    "get-record-with-review",
                    pathParameters(
                        parameterWithName("record_id").description("레코드 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.record_id").description("레코드 아이디"),
                        fieldWithPath("data.title").description("제목"),
                        fieldWithPath("data.content").description("내용"),
                        fieldWithPath("data.hits").description("조회수"),
                        fieldWithPath("data.part").description("파트"),
                        fieldWithPath("data.nick_name").description("닉네임"),
                        fieldWithPath("data.create_at").description("생성 일자"),

                        fieldWithPath("data.reviews[].review_id").description("리뷰 아이디"),
                        fieldWithPath("data.reviews[].content").description("리뷰 내용"),
                        fieldWithPath("data.reviews[].status").description("리뷰 상태"),
                        fieldWithPath("data.reviews[].nick_name").description("리뷰 작성자 닉네임"),
                        fieldWithPath("data.reviews[].create_at").description("리뷰 생성 일자"),

                        fieldWithPath("data.reviewers[].status").description("리뷰 신청자 상태"),
                        fieldWithPath("data.reviewers[].nick_name").description("리뷰 신청자 닉네임")
                    )
                )
            )
    }

    @Test
    @DisplayName("레코드 목록 조회 - 리뷰 받는 사람")
    fun getRevieweeRecords() {
        //given
        recordService.saveRecord(odoriReqDto, user!!)
        recordService.saveRecord(retroReqDto, user!!)
        recordService.saveRecord(techReqDtoIssue, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/reviewee?status=wait&page=0")
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
                    "get-reviewee-records",
                    requestParameters(
                        parameterWithName("status").description("레코드 상태"),
                        parameterWithName("page").description("페이지 번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.records.content[].record_id").description("레코드 아이디"),
                        fieldWithPath("data.records.content[].title").description("제목"),
                        fieldWithPath("data.records.content[].content").description("내용"),
                        fieldWithPath("data.records.content[].nick_name").description("닉네임"),
                        fieldWithPath("data.records.content[].create_at").description("생성 일자"),
                        fieldWithPath("data.records.content[].part").description("파트"),

                        *pageFieldWithPaths("data.records")
                    )
                )
            )
    }

    @Test
    @DisplayName("레코드 목록 조회 - 리뷰 하는 사람")
    fun getReviewerRecords() {
        //given
        val recordId1 = recordIds[0]
        val recordId2 = recordIds[1]

        var recordReviewReqDto = MockEntity.recordReviewReqDto(recordId1, "approve")
        recordReviewService.saveRecordReview(recordReviewReqDto, user!!)
        recordReviewReqDto = MockEntity.recordReviewReqDto(recordId2, "approve")
        recordReviewService.saveRecordReview(recordReviewReqDto, user!!)

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri/reviewer?status=approve&page=0")
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
                    "get-reviewer-records",
                    requestParameters(
                        parameterWithName("status").description("레코드 상태"),
                        parameterWithName("page").description("페이지 번호")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.records.content[].record_id").description("레코드 아이디"),
                        fieldWithPath("data.records.content[].title").description("제목"),
                        fieldWithPath("data.records.content[].content").description("내용"),
                        fieldWithPath("data.records.content[].nick_name").description("닉네임"),
                        fieldWithPath("data.records.content[].create_at").description("생성 일자"),
                        fieldWithPath("data.records.content[].part").description("파트"),

                        *pageFieldWithPaths("data.records")
                    )
                )
            )
    }

    private fun pageFieldWithPaths(path: String): Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("$path.pageable.sort.empty").description(""),
            fieldWithPath("$path.pageable.sort.unsorted").description(""),
            fieldWithPath("$path.pageable.sort.sorted").description(""),
            fieldWithPath("$path.pageable.offset").description(""),
            fieldWithPath("$path.pageable.pageSize").description(""),
            fieldWithPath("$path.pageable.pageNumber").description(""),
            fieldWithPath("$path.pageable.paged").description(""),
            fieldWithPath("$path.pageable.unpaged").description(""),

            fieldWithPath("$path.last").description(""),
            fieldWithPath("$path.totalPages").description(""),
            fieldWithPath("$path.totalElements").description(""),
            fieldWithPath("$path.first").description(""),
            fieldWithPath("$path.size").description(""),
            fieldWithPath("$path.number").description(""),

            fieldWithPath("$path.sort.empty").description(""),
            fieldWithPath("$path.sort.unsorted").description(""),
            fieldWithPath("$path.sort.sorted").description(""),

            fieldWithPath("$path.numberOfElements").description(""),
            fieldWithPath("$path.empty").description("")
        )
    }
}