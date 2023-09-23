package com.example.jhouse_server.domain.notification.service

import com.example.jhouse_server.domain.board.service.BoardService
import com.example.jhouse_server.domain.comment.service.CommentService
import com.example.jhouse_server.domain.notification.dto.NotificationReqDto
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class NotificationServiceImplTest @Autowired constructor(
    private val notificationService: NotificationService,
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val boardService: BoardService,
    private val commentService: CommentService
) {

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val userSignUpDto2 = MockEntity.testUserSignUpDto2()
    private val boardReqDto = MockEntity.boardReqDto()

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto)
        userService.signUp(userSignUpDto2)
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val user2 = userRepository.findByUserName(userSignUpDto2.userName).get()

        //board setting
        val boardId = boardService.createBoard(boardReqDto, user)

        //comment & notification setting
        val commentReqDto = MockEntity.commentReqDto(boardId)
        for(i in 0 .. 10) {
            commentService.createComment(commentReqDto, user2)
        }
    }

    @Test
    @DisplayName("알림 조회 테스트")
    fun getNotifications() {
        //given
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val pageRequest = PageRequest.of(0, 5)
        val req = NotificationReqDto(null, null)

        //when
        val content = notificationService.getNotifications(user, pageRequest, req)

        //then
        assertThat(content.nextId).isEqualTo(user.notifications[6].id)
        assertThat(content.notifications.size).isEqualTo(5)
    }

    @Test
    @DisplayName("알림 읽음 처리 테스트")
    fun updateNotification() {
        //given
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val notification = user.notifications[0]

        //when
        notificationService.updateNotification(notification.id, user)

        //then
        assertThat(notification.status).isEqualTo(true)
    }
}