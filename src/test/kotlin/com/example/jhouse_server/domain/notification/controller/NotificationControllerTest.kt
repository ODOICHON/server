package com.example.jhouse_server.domain.notification.controller

import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.comment.service.CommentService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationControllerTest @Autowired constructor(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val boardService: BoardService,
    private val commentService: CommentService
): ApiControllerConfig("/api/v1/notifications") {

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val userSignUpDto2 = MockEntity.testUserSignUpDto2()
    private val userSignInDto = MockEntity.testUserSignInDto()
    private val boardReqDto = MockEntity.boardReqDto()

    private var accessToken: String? = null
    private var user: User? = null
    private val notificationIds = mutableListOf<Long>()

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto)
        userService.signUp(userSignUpDto2)
        user = userRepository.findByEmail(userSignUpDto.email).get()
        val user2 = userRepository.findByEmail(userSignUpDto2.email).get()
        val tokenDto = userService.signIn(userSignInDto)
        accessToken = tokenDto.accessToken

        //board setting
        val boardId = boardService.createBoard(boardReqDto, user!!)

        //comment & notification setting
        val commentReqDto = MockEntity.commentReqDto(boardId)
        for(i in 0 .. 10) {
            commentService.createComment(commentReqDto, user2)
        }
        for(notification in user!!.notifications) {
            notificationIds.add(notification.id)
        }
    }

    @AfterEach
    fun after() {
        notificationIds.clear()
    }

    @Test
    @DisplayName("알림 조회 테스트")
    fun getNotifications() {
        //given

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("$uri?read=false&id=")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-notifications",
                    requestParameters(
                        parameterWithName("read").description("알림 읽음 필터링, null - 전부, false - 읽지 않음, true - 읽음"),
                        parameterWithName("id").description("초기요청시 null 입력 후, 이후 요청 부턴 응답의 nextId 기입")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data.nextId").description("다음 알림 아이디"),
                        fieldWithPath("data.notifications.content[].nick_name").description("닉네임"),
                        fieldWithPath("data.notifications.content[].board_id").description("게시글 아이디"),
                        fieldWithPath("data.notifications.content[].board_title").description("게시글 제목"),
                        fieldWithPath("data.notifications.content[].status").description("알림 읽음 상태, false - 읽지 않음, true - 읽음"),
                        fieldWithPath("data.notifications.content[].comment").description("댓글 내용"),
                        fieldWithPath("data.notifications.content[].comment_user").description("댓글 작성자 닉네임"),
                        fieldWithPath("data.notifications.content[].notification_id").description("알림 아이디"),

                        *pageFieldWithPaths("data.notifications")
                    )
                )
            )
    }

    @Test
    @DisplayName("알림 읽음 처리 테스트")
    fun updateNotification() {
        //given
        val id = notificationIds[0]

        //when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("$uri/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .characterEncoding("UTF-8")
        )

        //then
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "update-notification",
                    pathParameters(
                        parameterWithName("id").description("알림 아이디")
                    ),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("응답 메세지"),
                        fieldWithPath("data").description("알림 아이디")
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

            fieldWithPath("$path.last").description("마지막 페이지 여부"),
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