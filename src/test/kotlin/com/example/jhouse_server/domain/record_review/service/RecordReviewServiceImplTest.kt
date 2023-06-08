package com.example.jhouse_server.domain.record_review.service

import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.record_review.entity.RecordReviewStatus
import com.example.jhouse_server.domain.record_review.repository.RecordReviewRepository
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApplyStatus
import com.example.jhouse_server.domain.record_review_apply.repository.RecordReviewApplyRepository
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.exception.ApplicationException
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
class RecordReviewServiceImplTest @Autowired constructor(
    private val recordReviewService: RecordReviewService,
    private val recordReviewRepository: RecordReviewRepository,
    private val recordReviewApplyRepository: RecordReviewApplyRepository,
    private val recordService: RecordService,
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    private val userSignUpDto1 = MockEntity.testUserSignUpDto()
    private val userSignUpDto2 = MockEntity.testUserSignUpDto2()
    private val userSignUpDto3 = MockEntity.testUserSignUpDto3()
    private val odoriReqDto = MockEntity.odoriReqDto()

    private val userIds: MutableList<Long> = mutableListOf()
    private var recordId: Long? = null

    @BeforeEach
    fun before() {
        //user setting
        userService.signUp(userSignUpDto1)
        userService.signUp(userSignUpDto2)
        userService.signUp(userSignUpDto3)
        val user1 = userRepository.findByEmail(userSignUpDto1.email).get()
        val user2 = userRepository.findByEmail(userSignUpDto2.email).get()
        val user3 = userRepository.findByEmail(userSignUpDto3.email).get()
        user1.updateAuthority(Authority.ADMIN)
        user1.updateUserType(UserType.SERVER)
        user2.updateAuthority(Authority.ADMIN)
        user2.updateUserType(UserType.SERVER)
        user3.updateAuthority(Authority.ADMIN)
        user3.updateUserType(UserType.SERVER)

        userIds.add(user1.id)
        userIds.add(user2.id)
        userIds.add(user3.id)

        //record setting
        recordId = recordService.saveRecord(odoriReqDto, user1)
    }

    @Test
    @DisplayName("기본 테스트")
    fun basicTest() {
        //given

        //when
        val applies = recordReviewApplyRepository.findByRecordWithUserExcludeMine(recordId!!)

        //then
        assertThat(applies.size).isEqualTo(2)
        assertThat(applies.filter { it.status == RecordReviewApplyStatus.MINE }.size).isEqualTo(0)
        assertThat(applies.filter { it.status == RecordReviewApplyStatus.WAIT }.size).isEqualTo(2)
    }

    @Test
    @DisplayName("리뷰 저장 테스트")
    fun saveRecordReviewTest() {
        //given
        val recordReviewReqDtoReject = MockEntity.recordReviewReqDto(recordId!!, "reject")
        val recordReviewReqDtoApprove = MockEntity.recordReviewReqDto(recordId!!, "approve")
        val userId2 = userIds[1]
        val userId3 = userIds[2]
        val user2 = userRepository.findById(userId2).get()
        val user3 = userRepository.findById(userId3).get()
        val apply2 = recordReviewApplyRepository.findWithRecord(recordId!!, userId2).get()
        val apply3 = recordReviewApplyRepository.findWithRecord(recordId!!, userId3).get()

        //when
        val reviewId2 = recordReviewService.saveRecordReview(recordReviewReqDtoReject, user2)
        val reviewId3 = recordReviewService.saveRecordReview(recordReviewReqDtoApprove, user3)
        val review2 = recordReviewRepository.findById(reviewId2).get()
        val review3 = recordReviewRepository.findById(reviewId3).get()

        //then
        assertThat(apply2.status).isEqualTo(RecordReviewApplyStatus.REJECT)
        assertThat(review2.status).isEqualTo(RecordReviewStatus.REJECT)
        assertThat(review2.content).isEqualTo(recordReviewReqDtoReject.content)

        assertThat(apply3.status).isEqualTo(RecordReviewApplyStatus.APPROVE)
        assertThat(review3.status).isEqualTo(RecordReviewStatus.APPROVE)
        assertThat(review3.content).isEqualTo(recordReviewReqDtoApprove.content)

        assertThatThrownBy { recordReviewService.saveRecordReview(recordReviewReqDtoApprove, user3) }
            .isInstanceOf(ApplicationException::class.java)
    }

    @Test
    @DisplayName("리뷰 수정 테스트")
    fun updateRecordReviewTest() {
        //given
        val recordReviewReqDtoReject = MockEntity.recordReviewReqDto(recordId!!, "reject")
        val recordReviewReqDtoApprove = MockEntity.recordReviewReqDto(recordId!!, "approve")
        val userId = userIds[1]
        val user = userRepository.findById(userId).get()
        val apply = recordReviewApplyRepository.findWithRecord(recordId!!, userId).get()

        val reviewId = recordReviewService.saveRecordReview(recordReviewReqDtoReject, user)

        //when
        val recordReviewUpdateDto = MockEntity.recordReviewUpdateDto()
        recordReviewService.updateRecordReview(recordReviewUpdateDto, user, reviewId)
        val review = recordReviewRepository.findById(reviewId).get()

        //then
        assertThat(apply.status).isEqualTo(RecordReviewApplyStatus.APPROVE)
        assertThat(review.status).isEqualTo(RecordReviewStatus.APPROVE)
        assertThat(review.content).isEqualTo(recordReviewUpdateDto.content)

        assertThatThrownBy { recordReviewService.saveRecordReview(recordReviewReqDtoApprove, user) }
            .isInstanceOf(ApplicationException::class.java)
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    fun deleteRecordReview() {
        //given
        val recordReviewReqDtoReject = MockEntity.recordReviewReqDto(recordId!!, "reject")
        val recordReviewReqDtoApprove = MockEntity.recordReviewReqDto(recordId!!, "approve")
        val userId2 = userIds[1]
        val userId3 = userIds[2]
        val user2 = userRepository.findById(userId2).get()
        val user3 = userRepository.findById(userId3).get()

        val reviewId2 = recordReviewService.saveRecordReview(recordReviewReqDtoReject, user2)
        val reviewId3 = recordReviewService.saveRecordReview(recordReviewReqDtoApprove, user3)

        //when
        recordReviewService.deleteRecordReview(user2, reviewId2)
        val reviewOptional = recordReviewRepository.findById(reviewId2)

        //then
        assertThat(reviewOptional).isEmpty

        assertThatThrownBy { recordReviewService.deleteRecordReview(user3, reviewId3) }
            .isInstanceOf(ApplicationException::class.java)
    }
}