package com.example.jhouse_server.domain.board.controller

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.board.service.BoardService
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
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class BoardControllerTest @Autowired constructor(
    val boardService: BoardService,
    val boardRepository: BoardRepository,
    val userService: UserService,
    val userRepository: UserRepository,
) : ApiControllerConfig(uri = "/api/v1/boards") {
    private val userSignUpReqDto = MockEntity.testUserSignUpDto()
    private val userSignInReqDto = MockEntity.testUserSignInDto()
    private lateinit var tokenDto: TokenDto
    private var board: Long = 0L

    fun `로그인`() {
        if (!userRepository.existsByEmail(userSignUpReqDto.email)) {
            userService.signUp(userSignUpReqDto)
        }
        tokenDto = userService.signIn(userSignInReqDto)
    }

    @BeforeEach
    fun `게시글 더미 데이터 생성`() {
        로그인()
        val user = userRepository.findByEmail(userSignInReqDto.email)
        board = boardService.createBoard(MockEntity.boardReqDto(), user.get())
        val findBoard = boardRepository.findByIdOrThrow(board)
        findBoard.addComment(MockEntity.comment(findBoard, user.get()))
        boardRepository.save(findBoard)
    }

    @Test
    @DisplayName("게시글 조회")
    fun getBoardAll() {
        // given
        val uri = "$uri?category=INTRO"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-board",
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *pageResponseFieldSnippet()
                    )
                )
            )
    }

    private fun pageResponseFieldSnippet(): Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("content[].boardId").description("게시글 ID"),
            fieldWithPath("content[].code").description("HTML 정적 코드"),
            fieldWithPath("content[].title").description("게시글 제목"),
            fieldWithPath("content[].oneLineContent").description("한줄 소개"),
            fieldWithPath("content[].nickName").description("작성자 닉네임"),
            fieldWithPath("content[].createdAt").description("생성일시"),
            fieldWithPath("content[].imageUrl").description("이미지 주소 리스트"),
            fieldWithPath("content[].commentCount").description("댓글 수"),
            fieldWithPath("content[].category").description("말머리"),
            fieldWithPath("content[].prefixCategory").description("커뮤니티 대분류"),
            fieldWithPath("pageable.sort.empty").description(""),
            fieldWithPath("pageable.sort.unsorted").description(""),
            fieldWithPath("pageable.sort.sorted").description(""),
            fieldWithPath("pageable.offset").description(""),
            fieldWithPath("pageable.pageNumber").description(""),
            fieldWithPath("pageable.pageSize").description(""),
            fieldWithPath("pageable.unpaged").description(""),
            fieldWithPath("pageable.paged").description(""),
            fieldWithPath("last").description(""),
            fieldWithPath("totalPages").description(""),
            fieldWithPath("totalElements").description(""),
            fieldWithPath("size").description(""),
            fieldWithPath("number").description(""),
            fieldWithPath("sort.empty").description(""),
            fieldWithPath("sort.unsorted").description(""),
            fieldWithPath("sort.sorted").description(""),
            fieldWithPath("first").description(""),
            fieldWithPath("numberOfElements").description(""),
            fieldWithPath("empty").description(""),
        )
    }

    @Test
    @DisplayName("게시글 단일 조회")
    fun getBoardOne() {
        val boardId = board
        val uri = "$uri/{boardId}"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri, boardId)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions.andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "get-board-detail",
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *responseFieldSnippet()
                    )
                )
            )
    }

    private fun responseFieldSnippet(): Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("boardId").description("게시글 ID"),
            fieldWithPath("title").description("게시글 제목"),
            fieldWithPath("code").description("HTML 정적 코드"),
            fieldWithPath("nickName").description("작성자 닉네임"),
            fieldWithPath("createdAt").description("생성일시"),
            fieldWithPath("imageUrls").description("이미지 주소 리스트"),
            fieldWithPath("loveCount").description("좋아요 수"),
            fieldWithPath("category").description("말머리"),
            fieldWithPath("prefixCategory").description("게시글 대분류"),
            fieldWithPath("commentCount").description("댓글 수"),
            fieldWithPath("comments[].commentId").description("댓글 ID"),
            fieldWithPath("comments[].nickName").description("댓글 작성자"),
            fieldWithPath("comments[].content").description("댓글 내용"),
            fieldWithPath("comments[].createdAt").description("댓글 생성일시"),
        )
    }

    @Test
    @DisplayName("게시글 생성_자유")
    fun createBoard_default() {
        // given
        val req = MockEntity.boardDefaultReqDto()
        val uri = "$uri"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(uri)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "default-board-create",
                    requestFields(*createRequestFieldSnippet()),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    private fun createRequestFieldSnippet(): Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("title").description("게시글 제목"),
            fieldWithPath("code").description("DB에 저장할 html 정적 코드"),
            fieldWithPath("category").description("게시글 말머리"),
            fieldWithPath("imageUrls").description("이미지 url ',' 구분자로 넘겨주기 "),
            fieldWithPath("prefixCategory").description("게시글 타입, [DEFAULT, INTRO, ADVERTISEMENT]"),
            fieldWithPath("fixed").description("고정여부, 고정x false, 고정 true"),
        )
    }

    @Test
    @DisplayName("게시글 생성_홍보")
    fun createBoard_ads() {
        // given
        val req = MockEntity.boardAdsReqDto()
        val uri = "$uri"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(uri)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "ads-board-create",
                    requestFields(*createRequestFieldSnippet()),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    @Test
    @DisplayName("게시글 생성_소개")
    fun createBoard_intro() {
        // given
        val req = MockEntity.boardIntroReqDto()
        val uri = "$uri"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post(uri)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "intro-board-create",
                    requestFields(*createRequestFieldSnippet()),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    @Test
    @DisplayName("게시글 수정")
    fun updateBoard() {
        val req = MockEntity.boardReqDto()
        val boardId = board
        val uri = "$uri/{boardId}"

        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put(uri, boardId)
                .header(AUTHORIZATION, tokenDto.accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(req))
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "board-update",
                    requestFields(*createRequestFieldSnippet()),
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data").description("결과 데이터")
                    )
                )
            )
    }

    @Test
    @DisplayName("게시글 삭제")
    fun deleteBoard() {
        //given
        val boardId = board
        val uri = "$uri/{boardId}"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(uri, boardId)
                .header(AUTHORIZATION, tokenDto.accessToken)
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "board-delete",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                    )
                )
            )
    }
    @Test
    @DisplayName("홍보 게시글 말머리 조회")
    fun getCategory() {
        val uri = "$uri/category?name=DEFAULT"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "board-category",
                    responseFields(
                        fieldWithPath("code").description("결과 코드"),
                        fieldWithPath("message").description("결과 메세지"),
                        fieldWithPath("data[].code").description("영문 코드"),
                        fieldWithPath("data[].name").description("한글 코드명")
                    )
                )
            )
    }


    @Test
    @DisplayName("게시글 검색")
    fun getBoardAllWithKeyword() {
        val uri = "$uri/category/search?name=INTRO&keyword=짱구"
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        )
        resultActions
            .andExpect(status().isOk)
            .andDo(print())
            .andDo(
                document(
                    "board-search",
                    responseFields(
                        beneathPath("data").withSubsectionId("data"),
                        *pageResponseFieldSnippet()
                    )
                )
            )
    }
}