package com.example.jhouse_server.domain.comment.controller

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.comment.repository.CommentRepository
import com.example.jhouse_server.domain.comment.service.CommentService
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.findByIdOrThrow
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class CommentControllerTest @Autowired constructor(
    val commentService: CommentService,
    val commentRepository: CommentRepository,
    val boardService: BoardService,
    val boardRepository: BoardRepository,
    val userService: UserService,
    val userRepository: UserRepository
) : ApiControllerConfig("/api/v1/comments") {
    private val userSignUpReqDto = MockEntity.testUserSignUpDto()
    private val userSignInReqDto = MockEntity.testUserSignInDto()
    private lateinit var tokenDto: TokenDto
    private var board: Long = 0L
    private var comment: Long = 0L

    fun `로그인`() {
        if (!userRepository.existsByUserName(userSignUpReqDto.userName)) {
            userService.signUp(userSignUpReqDto)
        }
        tokenDto = userService.signIn(userSignInReqDto)
    }

    @BeforeEach
    @Transactional
    fun `게시글 더미 데이터 생성`() {
        로그인()
        val user = userRepository.findByUserName(userSignInReqDto.userName).get()
        board = boardService.createBoard(MockEntity.boardReqDto(), user)
        val findBoard = boardRepository.findByIdOrThrow(board)
        comment = commentService.createComment(MockEntity.commentReqDto(board), user)
        findBoard.addComment(MockEntity.comment(findBoard, user))
        boardRepository.save(findBoard)
    }
    @Test
    @DisplayName("게시글이 갖는 댓글 리스트 조회")
    fun get_comment_all() {
        // given
        val uri = "$uri/{boardId}"
        val boardId = board
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri, boardId)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "get-comment-all",
                    PayloadDocumentation.responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data[].commentId").description("댓글 ID"),
                        fieldWithPath("data[].nickName").description("댓글 작성자"),
                        fieldWithPath("data[].content").description("댓글 내용"),
                        fieldWithPath("data[].createdAt").description("댓글 생성일시"),
                    )
                )
            )
    }
    @Test
    @DisplayName("댓글 작성")
    fun create_comment() {
        val uri = "$uri"
        val req = MockEntity.commentReqDto(board)
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(uri)
                .header(HttpHeaders.AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "comment-create",
                    PayloadDocumentation.requestFields(
                        fieldWithPath("boardId").description("게시글 ID"),
                        fieldWithPath("content").description("댓글 내용")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }
    @Test
    @DisplayName("댓글 수정")
    fun update_comment() {
        val uri = "$uri/{commentId}"
        val commentId = comment
        val req = MockEntity.commentReqDto(board)
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(uri, commentId)
                .header(HttpHeaders.AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "comment-update",
                    PayloadDocumentation.requestFields(
                        fieldWithPath("boardId").description("게시글 ID"),
                        fieldWithPath("content").description("댓글 내용")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }
    @Test
    @DisplayName("댓글 삭제")
    fun delete_comment() {
        val uri = "$uri/{commentId}"
        val commentId = comment
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(uri, commentId)
                .header(HttpHeaders.AUTHORIZATION, tokenDto.accessToken)
        )
        resultActions
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "comment-delete",
                    PayloadDocumentation.responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지")
                    )
                )
            )
    }
}