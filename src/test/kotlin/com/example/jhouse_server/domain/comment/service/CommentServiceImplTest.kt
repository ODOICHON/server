package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.comment.repository.CommentRepository
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
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
internal class CommentServiceImplTest @Autowired constructor(
    val commentService: CommentService,
    val commentRepository: CommentRepository,
    val boardService: BoardService,
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
    @DisplayName("댓글 작성")
    fun create_comment() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val findUser = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val req = MockEntity.commentReqDto(게시글_작성())
        // when
        val saved = commentService.createComment(req, findUser)
        // then
        val findComment = commentRepository.findByIdOrThrow(saved)
        assertThat(findComment.content).isEqualTo(req.content)
    }

    @Test
    @DisplayName("댓글 수정")
    fun update_comment() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val findUser = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val already = MockEntity.commentReqDto(게시글_작성())
        val saved = commentService.createComment(already, findUser)
        val req = MockEntity.updateCommentReqDto(게시글_작성())
        // when
        val updated = commentService.updateComment(saved, req, findUser)
        // then
        val findComment = commentRepository.findByIdOrThrow(updated)
        assertThat(findComment.content).isEqualTo(req.content)
    }

    @Test
    @DisplayName("댓글 삭제")
    fun delete_comment() {
        // given
        로그인(userSignUpReqDto.userName, userSignUpReqDto.password)
        val findUser = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val already = MockEntity.commentReqDto(게시글_작성())
        val saved = commentService.createComment(already, findUser)
        // when - then
        commentService.deleteComment(saved, findUser)
        assertThrows(ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)::class.java) {commentService.deleteComment(saved, findUser)}
        }

    @Test
    @DisplayName("댓글 삭제 - 권한 없음")
    fun delete_comment_unauthorized() {
        로그인(testSignUpReqDto.userName, testSignUpReqDto.password)
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val unWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        val already = MockEntity.commentReqDto(게시글_작성())
        val saved = commentService.createComment(already, writer)
        // when-then
        assertThrows(ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)::class.java) { commentService.deleteComment(saved, unWriter)}
    }


}