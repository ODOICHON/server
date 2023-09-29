package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.entity.DealState
import com.example.jhouse_server.domain.house.entity.HouseReviewStatus
import com.example.jhouse_server.domain.house.entity.RentalType
import com.example.jhouse_server.domain.house.repository.DealRepository
import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.scrap.repository.ScrapRepository
import com.example.jhouse_server.domain.scrap.service.ScrapService
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.exception.ReqValidationException
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.MockEntity.Companion.houseInvalidReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.houseReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.houseTmpReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.houseUpdateReqDto
import com.example.jhouse_server.global.util.MockEntity.Companion.reportReqDto
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
internal class HouseServiceImplTest @Autowired constructor(
    val houseService: HouseService,
    val houseRepository: HouseRepository,
    val userService: UserService,
    val userRepository: UserRepository,
    val scrapService: ScrapService,
    val scrapRepository: ScrapRepository,
    val dealRepository: DealRepository,
) {
    private val userSignUpReqDto = MockEntity.testUserSignUpDto2()
    private val testSignUpReqDto = MockEntity.testUserSignUpDto()
    private val adminSignUpReqDto = MockEntity.testUserSignUpDto3()

    @BeforeEach
    fun `회원가입`() {
        // default user
        userService.signUp(userSignUpReqDto)
        // another user ( for agent )
        userService.signUp(testSignUpReqDto)
        val agent = userRepository.findByUserName(testSignUpReqDto.userName).get()
        agent.updateUserType(UserType.AGENT)
        // admin user
        userService.signUp(adminSignUpReqDto)
        val admin = userRepository.findByUserName(adminSignUpReqDto.userName).get()
        admin.updateAuthority(Authority.ADMIN)
    }
    fun `로그인`(email: String, password: String) {
        userService.signIn(UserSignInReqDto(email, password))
    }
    @Test
    @DisplayName("빈집 게시글 생성 - 일반 유저")
    fun createHouse_user() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()

        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)
        val house = houseRepository.findByIdOrThrow(houseId)

        // then
        assertThat(house.rentalType).isEqualTo(houseReqDto().rentalType)
        assertThat(house.applied).isEqualTo(HouseReviewStatus.APPLY)

    }

    @Test
    @DisplayName("빈집 게시글 생성 - 공인중개사 유저")
    fun createHouse_agent() {
        // given
        val writer = userRepository.findByUserName(testSignUpReqDto.userName).get()

        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)
        val house = houseRepository.findByIdOrThrow(houseId)

        // then
        assertThat(house.user.userType).isEqualTo(UserType.AGENT)
        assertThat(house.applied).isEqualTo(HouseReviewStatus.APPROVE)
    }

    @Test
    @DisplayName("빈집 게시글 생성 - 관리자 유저")
    fun createHouse_admin() {
        // given
        val writer = userRepository.findByUserName(adminSignUpReqDto.userName).get()

        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)
        val house = houseRepository.findByIdOrThrow(houseId)

        // then
        assertThat(house.user.authority).isEqualTo(Authority.ADMIN)
        assertThat(house.applied).isEqualTo(HouseReviewStatus.APPROVE)
    }

    @Test
    @DisplayName("빈집 게시글 생성 - 유효성 검사 실패")
    fun createHouse_invalid() {
        // given
        val writer = userRepository.findByUserName(adminSignUpReqDto.userName).get()

        // when
        assertThrows(ReqValidationException("유효성 검사 실패")::class.java) {
            houseService.createHouse(houseInvalidReqDto(), writer)
        }
    }

    @Test
    @DisplayName("빈집 게시글 임시저장 - 일반 유저")
    fun createHouse_tmp_default() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()

        // when
        val houseId = houseService.createHouse(houseTmpReqDto(), writer)
        val house = houseRepository.findByIdOrThrow(houseId)

        // then
        assertThat(house.tmpYn).isTrue
        assertThat(house.applied).isNull()
    }

    @Test
    @DisplayName("임시저장된 빈집 게시글 수정 - 일반 유저")
    fun updateHouse_tmp_default() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()

        // when
        val houseId = houseService.createHouse(houseTmpReqDto(), writer)
        val updated = houseService.updateHouse(houseId, houseUpdateReqDto(), writer)
        val house = houseRepository.findByIdOrThrow(updated)

        // then
        assertThat(house.tmpYn).isFalse
        assertThat(house.applied).isEqualTo(HouseReviewStatus.APPLY)
    }

    @Test
    @DisplayName("임시저장된 빈집 게시글 수정 - 공인중개사 유저")
    fun updateHouse_tmp_agent() {
        // given
        val writer = userRepository.findByUserName(testSignUpReqDto.userName).get()

        // when
        val houseId = houseService.createHouse(houseTmpReqDto(), writer)
        val updated = houseService.updateHouse(houseId, houseUpdateReqDto(), writer)
        val house = houseRepository.findByIdOrThrow(updated)

        // then
        assertThat(house.tmpYn).isFalse
        assertThat(house.applied).isEqualTo(HouseReviewStatus.APPROVE)
    }

    @Test
    @DisplayName("일반 유저의 임시저장된 빈집 게시글 수정 권한 없음 - 타 유저")
    fun updateHouse_tmp_default_unauthorized() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val anotherWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        // when
        val houseId = houseService.createHouse(houseTmpReqDto(), writer)

       assertThrows(ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)::class.java) {
           houseService.updateHouse(houseId, houseUpdateReqDto(), anotherWriter)
       }
    }

    @Test
    @DisplayName("빈집 게시글 수정 권한 없음 - 타 유저")
    fun updateHouse_default_unauthorized() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val anotherWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)

        assertThrows(ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)::class.java) {
            houseService.updateHouse(houseId, houseUpdateReqDto(), anotherWriter)
        }
    }

    @Test
    @DisplayName("빈집 게시글 삭제 - 작성자 본인")
    fun deleteHouse_default() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)
        houseService.deleteHouse(houseId, writer)
        // then
        val findHouse = houseRepository.findByIdOrNull(houseId)
        assertThat(findHouse!!.useYn).isFalse
    }

    @Test
    @DisplayName("빈집 게시글 삭제 - 관리자")
    fun deleteHouse_admin() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val admin = userRepository.findByUserName(adminSignUpReqDto.userName).get()
        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)
        houseService.deleteHouse(houseId, admin)
        // then
        val findHouse = houseRepository.findByIdOrNull(houseId)
        assertThat(findHouse!!.useYn).isFalse
    }

    @Test
    @DisplayName("빈집 게시글 삭제 권한 없음 - 타 유저")
    fun deleteHouse_default_unauthorized() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val anotherWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        // when
        val houseId = houseService.createHouse(houseReqDto(), writer)

        assertThrows(ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)::class.java) {
            houseService.deleteHouse(houseId, anotherWriter)
        }
    }

    @Test
    @DisplayName("빈집 게시글 신고 - 타 유저")
    fun reportHouse_another_user() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val anotherWriter = userRepository.findByUserName(testSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        // when
        houseService.reportHouse(houseId, reportReqDto(), anotherWriter)
        val findHouse = houseRepository.findByIdOrThrow(houseId)

        // then
        assertThat(findHouse.reported).isTrue
    }

    @Test
    @DisplayName("빈집 게시글 신고 - 작성자 본인")
    fun reportHouse_mine() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        // when - then
        assertThrows(ApplicationException(ErrorCode.DONT_REPORT_HOUSE_MINE)::class.java) {
            houseService.reportHouse(houseId, reportReqDto(), writer)
        }
    }

    @Test
    @DisplayName("빈집 게시글 상세 조회")
    fun getHouse_unSignIn() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        // when - then
        val house = houseService.getHouseOne(houseId)

        assertThat(house.agentName).isEqualTo(houseReqDto().agentName)
    }

    @Test
    @DisplayName("빈집 게시글 상세 조회 - 로그인")
    fun getHouse_signIn_unScrapped() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        // when - then
        val house = houseService.getHouseOneWithUser(houseId, writer)

        assertThat(house.isScraped).isFalse
    }

    @Test
    @DisplayName("빈집 게시글 상세 조회 - 로그인")
    fun getHouse_signIn_scrapped() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val anotherUser = userRepository.findByUserName(testSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        scrapService.scrapHouse(houseId, anotherUser)
        // when
        val house = houseService.getHouseOneWithUser(houseId, anotherUser)
        // then
        assertThat(house.isScraped).isTrue
    }

    @Test
    @DisplayName("임시저장된 빈집 게시글 목록 조회 - 작성자 본인")
    fun getTmpSaveHouseAll_signIn() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        createTmpHouseAll()
        val pageable = PageRequest.of(0, 8)
        // when
        val house = houseService.getTmpSaveHouseAll(writer, pageable)
        // then
        assertThat(house.totalElements).isEqualTo(12)
    }

    private fun createTmpHouseAll() {
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        for(i in 0 until 12) {
            houseService.createHouse(houseTmpReqDto(), writer)
        }
    }

    @Test
    @DisplayName("빈집 게시글 목록 조회 - 검색 필터링 [ 매매, 경기, 추천태그 리스트, '' ]")
    fun getHouseAll() {
        // given
        val houseListDto = HouseListDto(RentalType.SALE.name, "경기", listOf(), null, DealState.ONGOING.name)
        createHouseAll()
        val pageable = PageRequest.of(0, 8)
        // when
        val house = houseService.getHouseAll(houseListDto, pageable)
        // then
        assertThat(house.totalElements).isEqualTo(0)
    }

    @Test
    @DisplayName("빈집 게시글 목록 조회 - 검색 필터링 [ 매매, 전라, '' ]")
    fun getHouseAll_another_city() {
        // given
        val houseListDto = HouseListDto(RentalType.SALE.name, "전라", listOf(), null, DealState.ONGOING.name)
        createHouseAll()
        val pageable = PageRequest.of(0, 8)
        // when
        val house = houseService.getHouseAll(houseListDto, pageable)
        // then
        assertThat(house.totalElements).isEqualTo(0)
    }

    @Test
    @DisplayName("빈집 게시글 목록 조회 - 검색 필터링 [ 전세, 수도권, '' ]")
    fun getHouseAll_another_rentalType() {
        // given
        val houseListDto = HouseListDto(RentalType.JEONSE.name, "수도권", listOf(), null, DealState.ONGOING.name)
        createHouseAll()
        val pageable = PageRequest.of(0, 8)
        // when
        val house = houseService.getHouseAll(houseListDto, pageable)
        // then
        assertThat(house.totalElements).isEqualTo(0)
    }

    @Test
    @DisplayName("빈집 게시글 목록 조회 - 검색 필터링 [ 매매, 수도권, '행복부동산' ]")
    fun getHouseAll_with_keyword() {
        // given
        val houseListDto = HouseListDto(RentalType.SALE.name, "수도권", listOf(), "행복부동산", DealState.ONGOING.name)
        createHouseAll()
        val pageable = PageRequest.of(0, 8)
        // when
        val house = houseService.getHouseAll(houseListDto, pageable)
        // then
        assertThat(house.totalElements).isEqualTo(12)
    }

    @Test
    @DisplayName("빈집 거래 상태 변경")
    fun update_status() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        val req = MockEntity.updateStatus("테스트유저2")
        // when
        houseService.updateStatus(writer, houseId, req)
        // then
        assertThat(dealRepository.findByHouseId(houseId).get()).isNotNull
    }

    @Test
    @DisplayName("빈집 거래 상태 변경 - 닉네임 공백이거나 null일 경우")
    fun update_status_nickName_isBlank() {
        // given
        val writer = userRepository.findByUserName(userSignUpReqDto.userName).get()
        val houseId = houseService.createHouse(houseReqDto(), writer)
        val req = MockEntity.updateStatus("")
        // when
        // then
        assertThrows(ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)::class.java) {
            houseService.updateStatus(writer, houseId, req)
        }
    }

    private fun createHouseAll() {
        val writer = userRepository.findByUserName(adminSignUpReqDto.userName).get()
        for(i in 0 until 12) {
            houseService.createHouse(houseReqDto(), writer)
        }
    }
}