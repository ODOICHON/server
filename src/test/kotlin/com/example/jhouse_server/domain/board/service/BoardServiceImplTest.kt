package com.example.jhouse_server.domain.board.service

import com.example.jhouse_server.domain.board.BoardListDto
import com.example.jhouse_server.domain.board.BoardPreviewListDto
import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.user.dto.UserSignInReqDto
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
internal class BoardServiceImplTest @Autowired constructor(
    val boardService: BoardService,
    val boardRepository: BoardRepository,
    val userService: UserService,
    val userRepository: UserRepository,
) {
    private val userSignUpReqDto = MockEntity.testUserSignUpDto2()
    private val testSignUpReqDto = MockEntity.testUserSignUpDto()
    @BeforeEach
    fun `회원가입`() {
        // default user
        userService.signUp(userSignUpReqDto)
        // another user
        userService.signUp(testSignUpReqDto)
    }
    fun `로그인`(email: String, password: String) {
        userService.signIn(UserSignInReqDto(email, password))
    }
    @Test
    @DisplayName("자유 게시글 작성")
    fun create_default_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val findUser = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val req = MockEntity.boardDefaultReqDto()
        // when
        val saved = boardService.createBoard(req, findUser)
        // then
        val findBoard = boardRepository.findByIdOrThrow(saved)
        assertThat(findBoard.prefixCategory).isEqualTo(PrefixCategory.DEFAULT)
    }
    @Test
    @DisplayName("홍보 게시글 작성")
    fun create_advertisement_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val findUser = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val req = MockEntity.boardAdsReqDto()
        // when
        val saved = boardService.createBoard(req, findUser)
        // then
        val findBoard = boardRepository.findByIdOrThrow(saved)
        assertThat(findBoard.prefixCategory).isEqualTo(PrefixCategory.ADVERTISEMENT)
    }
    @Test
    @DisplayName("소개 게시글 작성")
    fun create_intro_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val findUser = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val req = MockEntity.boardIntroReqDto()
        // when
        val saved = boardService.createBoard(req, findUser)
        // then
        val findBoard = boardRepository.findByIdOrThrow(saved)
        assertThat(findBoard.prefixCategory).isEqualTo(PrefixCategory.INTRO)
    }
    @Test
    @DisplayName("게시글 수정_말머리 변경")
    fun update_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val saved = boardService.createBoard(MockEntity.boardReqDto(), writer)
        val req = MockEntity.boardUpdateReqDto()
        // when
        val updated = boardService.updateBoard(saved, req, writer)
        // then
        val findBoard = boardRepository.findByIdOrNull(updated)
        assertThat(findBoard!!.category).isNotEqualTo(MockEntity.boardReqDto().category)
    }
    @Test
    @DisplayName("게시글 수정_권한없음")
    fun update_board_unauthorized() {
        // given
        로그인(testSignUpReqDto.userName, testSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val unWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        val saved = boardService.createBoard(MockEntity.boardReqDto(), writer)
        val req = MockEntity.boardUpdateReqDto()
        // when - then
        assertThrows(ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)::class.java) {
            boardService.updateBoard(saved, req, unWriter)
        }
    }
    @Test
    @DisplayName("게시글 삭제")
    fun delete_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val saved = boardService.createBoard(MockEntity.boardReqDto(), writer)
        // when
        boardService.deleteBoard(saved, writer)
        // then
        val findBoard = boardRepository.findByIdOrNull(saved)
        assertThat(findBoard!!.useYn).isFalse
    }
    @Test
    @DisplayName("게시글 삭제_권한 오류")
    fun delete_board_unauthorized() {
        // given
        로그인(testSignUpReqDto.userName, testSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val unWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        val saved = boardService.createBoard(MockEntity.boardReqDto(), writer)
        // when-then
        assertThrows(ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)::class.java) { boardService.deleteBoard(saved, unWriter) }
    }

    @Test
    @DisplayName("게시판별 말머리 조회")
    fun get_category() {
        // given
        val name = PrefixCategory.ADVERTISEMENT.name
        // when
        val res = boardService.getCategory(name)
        // then
        assertThat(res[0].code).isEqualTo(BoardCategory.INTERIOR.value)
    }

    @Test
    @DisplayName("게시판 말머리 조회_실패")
    fun get_category_fail() {
        // given
        val name = ""
        // when
        val res = boardService.getCategory(name)
        // then
        assertThat(res).isEmpty()
    }

    @Test
    @DisplayName("메인페이지 게시글 조회")
    fun get_board_preview_all() {
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        for (i in 0 until 5) {
            boardService.createBoard(MockEntity.boardReqDto(), writer)
        }
        val boardPreviewListDto = BoardPreviewListDto("INTRO", "TREND", 5)
        val result = boardService.getBoardPreviewAll(boardPreviewListDto)
        assertThat(result.size).isEqualTo(5)
    }

    @Test
    @DisplayName("게시글 조회")
    fun get_board_all() {
        // given
        val pageable = PageRequest.of(0, 8)
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        for (i in 0 until 8) {
            boardService.createBoard(MockEntity.boardReqDto(), writer)
        }
        val boardListDto = BoardListDto("INTRO", "TREND", null, null)
        // when
        val res = boardService.getBoardAll(boardListDto, pageable)
        // then
        assertThat(res.content.size).isEqualTo(8)
    }

    @Test
    @DisplayName("게시글 조회_미리보기문구")
    fun get_board_all_one_line_content() {
        // given
        val pageable = PageRequest.of(0, 8)
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        for (i in 0 until 8) {
            boardService.createBoard(MockEntity.boardReqDto(), writer)
        }
        val boardListDto = BoardListDto("INTRO", "TREND", null, null)
        // when
        val res = boardService.getBoardAll(boardListDto, pageable)
        // then
        assertThat(res.content[0].oneLineContent.length).isLessThan(201)
    }
    @Test
    @DisplayName("게시글 조회_삭제된 게시글 미노출")
    fun get_board_all_delete() {
        // given
        val pageable = PageRequest.of(0, 8)
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        var lastId = 0L
        for (i in 0 until 8) {
            lastId = boardService.createBoard(MockEntity.boardReqDto(), writer)
        }
        boardService.deleteBoard(lastId, writer)
        val boardListDto = BoardListDto("INTRO", "TREND", null, null)
        // when
        val res = boardService.getBoardAll(boardListDto, pageable)
        // then
        assertThat(res.content.size).isEqualTo(7)
    }
    @Test
    @DisplayName("게시글 상세 조회")
    fun get_board_detail() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val saved = boardService.createBoard(MockEntity.boardReqDto(), writer)
        // when
        val res = boardService.getBoardOne(saved)
        // then
        val findBoard = boardRepository.findByIdOrThrow(saved)
        assertThat(findBoard.title).isEqualTo(res.title)
    }
}