package com.example.jhouse_server.domain.record_comment.service

import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.record_comment.repository.RecordCommentRepository
import com.example.jhouse_server.domain.user.entity.AdminType
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class RecordCommentServiceImplTest @Autowired constructor(
    private val recordCommentService: RecordCommentService,
    private val recordCommentRepository: RecordCommentRepository,
    private val recordService: RecordService,
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val odoriReqDto = MockEntity.odoriReqDto()

    private var userId: Long? = null
    private var recordId: Long? = null

    private val DELETE_MESSAGE = "삭제된 댓글입니다."

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto)
        val user = userRepository.findByEmail(userSignUpDto.email).get()
        user.updateAuthority(Authority.ADMIN)
        user.updateAdminType(AdminType.SERVER)
        userId = user.id

        //record setting
        recordId = recordService.saveRecord(odoriReqDto, user)
    }

    @Test
    @DisplayName("댓글 저장 테스트")
    fun saveRecordCommentTest() {
        //given
        val user = userRepository.findById(userId!!).get()
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)

        //when
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user)
        val comment = recordCommentRepository.findById(commentId).get()

        //then
        assertThat(comment.content).isEqualTo(recordCommentReqDto.content)
        assertThat(comment.ref).isEqualTo(1)
        assertThat(comment.step).isEqualTo(1)
        assertThat(comment.level).isEqualTo(1)
        assertThat(comment.allChildrenSize).isEqualTo(0)
    }

    @Test
    @DisplayName("대댓글 저장 테스트")
    fun saveRecordCommentTest2() {
        //given
        val user = userRepository.findById(userId!!).get()
        val recordParentCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)
        val parentCommentId = recordCommentService.saveRecordComment(recordParentCommentReqDto, user)
        val parentComment = recordCommentRepository.findById(parentCommentId).get()
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, parentCommentId)

        //when
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user)
        val comment = recordCommentRepository.findById(commentId).get()

        //then
        assertThat(parentComment.content).isEqualTo(recordParentCommentReqDto.content)
        assertThat(parentComment.ref).isEqualTo(1)
        assertThat(parentComment.step).isEqualTo(1)
        assertThat(parentComment.level).isEqualTo(1)
        assertThat(parentComment.allChildrenSize).isEqualTo(1)

        assertThat(comment.content).isEqualTo(recordCommentReqDto.content)
        assertThat(comment.ref).isEqualTo(1)
        assertThat(comment.step).isEqualTo(2)
        assertThat(comment.level).isEqualTo(2)
        assertThat(comment.allChildrenSize).isEqualTo(0)
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    fun updateRecordCommentTest() {
        //given
        val user = userRepository.findById(userId!!).get()
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)
        val recordCommentUpdateDto = MockEntity.recordCommentUpdateDto()
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user)

        //when
        recordCommentService.updateRecordComment(recordCommentUpdateDto, user, commentId)
        val comment = recordCommentRepository.findById(commentId).get()

        //then
        assertThat(comment.content).isEqualTo(recordCommentUpdateDto.content)
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    fun deleteRecordCommentTest() {
        //given
        val user = userRepository.findById(userId!!).get()
        val recordCommentReqDto = MockEntity.recordCommentReqDto(recordId!!, null)
        val commentId = recordCommentService.saveRecordComment(recordCommentReqDto, user)

        //when
        recordCommentService.deleteRecordComment(user, commentId)
        val comment = recordCommentRepository.findById(commentId).get()

        //then
        assertThat(comment.content).isEqualTo(DELETE_MESSAGE)
    }
}