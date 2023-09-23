package com.example.jhouse_server.domain.love.service

import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.love.repository.LoveRepository
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
internal class LoveServiceImplTest @Autowired constructor(
    val loveService: LoveService,
    val loveRepository: LoveRepository,
    val boardService: BoardService,
    val boardRepository: BoardRepository,
    val userService: UserService,
    val userRepository: UserRepository
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
    fun `게시글_작성`() : Long {
        val writer = userRepository.findByUserName(testSignUpReqDto.userName).get()
        return boardService.createBoard(MockEntity.boardReqDto(), writer)
    }

    @Test
    @DisplayName("게시글 좋아요")
    fun love_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val board = 게시글_작성()
        val user = userRepository.findByUserName(userSignUpReqDto.userName).get()
        // when
        loveService.loveBoard(board, user)
        // then
        val findBoard = boardRepository.findByIdOrThrow(board)
        val findLoved = loveRepository.findByUserAndBoard(user, findBoard)
        assertThat(findLoved.board).isEqualTo(findBoard)
    }

    @Test
    @DisplayName("게시글 좋아요 취소")
    fun hate_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val board = 게시글_작성()
        val user = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val loved = loveService.loveBoard(board, user)
        // when
        loveService.hateBoard(board, user)
        val findBoard = boardRepository.findById(board)
        assertThat(findBoard.get().love.filter { it.id == loved }).isEmpty()
    }

    @Test
    @DisplayName("게시글 좋아요 여부 확인")
    fun is_loved_board() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val user = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val board = 게시글_작성()
        loveService.loveBoard(board, user)
        // when
        val isLoved = loveService.isLovedBoard(board, user)
        // then
        assertThat(isLoved).isTrue
    }
}