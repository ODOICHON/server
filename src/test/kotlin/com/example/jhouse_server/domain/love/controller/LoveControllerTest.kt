package com.example.jhouse_server.domain.love.controller

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.love.entity.Love
import com.example.jhouse_server.domain.love.service.LoveService
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.util.ApiControllerConfig
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.findByIdOrThrow
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
internal class LoveControllerTest @Autowired constructor(
    val loveService: LoveService,
    val boardService: BoardService,
    val boardRepository: BoardRepository,
    val userService: UserService,
    val userRepository: UserRepository,
) : ApiControllerConfig(uri = "/api/v1/loves") {
    private val userSignUpReqDto = MockEntity.testUserSignUpDto()
    private val userSignInReqDto = MockEntity.testUserSignInDto()
    private lateinit var tokenDto: TokenDto
    private var board: Long = 0L
    private var lovedBoard: Long = 0L

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
        lovedBoard = boardService.createBoard(MockEntity.boardReqDto(), user)
        val findBoard = boardRepository.findByIdOrThrow(lovedBoard)
        findBoard.addLove(Love(user, findBoard))
        boardRepository.save(findBoard)
    }

    @Test
    @DisplayName("게시글 좋아요")
    fun love_board() {
        val uri = "$uri/{boardId}"
        val boardId = board
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(uri, boardId)
                .header(HttpHeaders.AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "love-board",
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("결과 메세지"),
                        PayloadDocumentation.fieldWithPath("data").description("좋아요 ID"),
                    )
                )
            )
    }

    @Test
    @DisplayName("게시글 좋아요 취소")
    fun hate_board() {
        val uri = "$uri/{boardId}"
        val boardId = lovedBoard
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(uri, boardId)
                .header(HttpHeaders.AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "hate-board",
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("결과 메세지"),
                    )
                )
            )
    }

    @Test
    @DisplayName("게시글 좋아요 여부 확인")
    fun is_love_board() {
        val uri = "$uri/{boardId}"
        val boardId = board
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri, boardId)
                .header(HttpHeaders.AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(MockMvcResultHandlers.print())
            .andDo(
                MockMvcRestDocumentation.document(
                    "is-love-board",
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").description("결과 코드"),
                        PayloadDocumentation.fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }
}