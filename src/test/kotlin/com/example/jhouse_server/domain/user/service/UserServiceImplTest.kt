package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.DefaultUser
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.entity.WithdrawalReason
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.MockEntity
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class UserServiceImplTest @Autowired constructor(
        val tokenProvider: TokenProvider,
        val userRepository: UserRepository,
        val userService: UserService
) {

    private val UPDATE_NICK_NAME: String = "닉네임변경"

    private val UPDATE_PASSWORD: String = "!123abcdefg"

    private val userSignUpDto = MockEntity.testUserSignUpDto()

    fun userSignInDto(userName: String, password: String): UserSignInReqDto {
        return UserSignInReqDto(
                userName,
                password
        )
    }

    @Test
    @DisplayName("회원가입 테스트")
    fun signUpTest() {
        //given

        //when
        userService.signUp(userSignUpDto)

        //then
        val findUser = userRepository.findByUserName(userSignUpDto.userName).get()
        assertThat(findUser.userName).isEqualTo(userSignUpDto.userName)
        assertThat(findUser.nickName).isEqualTo(userSignUpDto.nickName)
        assertThat(findUser.phoneNum).isEqualTo(userSignUpDto.phoneNum)
        assertThat(findUser.profileImageUrl).isEqualTo(DefaultUser().profileImageUrl)
    }

    @Test
    @DisplayName("로그인 테스트")
    fun signInTest() {
        //given
        userService.signUp(userSignUpDto)
        val userSignInDto = userSignInDto(userSignUpDto.userName, userSignUpDto.password)

        //when
        val tokenDto = userService.signIn(userSignInDto)
        val bearerToken = tokenDto.accessToken
        val accessToken = tokenProvider.resolveToken(bearerToken).toString()

        //then
        val findUser = userRepository.findByUserName(userSignInDto.userName).get()
        assertThat(findUser.userName).isEqualTo(tokenProvider.getSubject(accessToken))
        assertThat(findUser.authority).isEqualTo(tokenProvider.getAuthority(accessToken))
    }

    @Test
    @DisplayName("닉네임 변경 테스트")
    fun updateNickName() {
        //given
        userService.signUp(userSignUpDto)
        val user = userRepository.findByUserName(userSignUpDto.userName).get()

        //when
        userService.updateNickName(user, UPDATE_NICK_NAME)

        //then
        val findUser = userRepository.findByIdOrThrow(user.id)
        assertThat(findUser.nickName).isEqualTo(UPDATE_NICK_NAME)
    }

    @Test
    @DisplayName("비밀번호 변경 테스트")
    fun updatePassword() {
        //given
        userService.signUp(userSignUpDto)
        val user = userRepository.findByUserName(userSignUpDto.userName).get()

        //when
        userService.updatePassword(user, UPDATE_PASSWORD)

        //then
        val userSignInDto = userSignInDto(user.userName, UPDATE_PASSWORD)
        val tokenDto = userService.signIn(userSignInDto)
        val bearerToken = tokenDto.accessToken
        val accessToken = tokenProvider.resolveToken(bearerToken).toString()
        val userName = tokenProvider.getSubject(accessToken)
        val findUser = userRepository.findByUserName(userName).orElseThrow()

        assertThat(user).isEqualTo(findUser)
    }

    @Test
    @DisplayName("회원정보 수정 테스트")
    fun update() {
        //given
        userService.signUp(userSignUpDto)
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val password = user.password
        val userUpdateReqDto = MockEntity.userUpdateReqDto("")

        //when
        userService.update(user, userUpdateReqDto)

        //then
        assertThat(user.nickName).isEqualTo(userUpdateReqDto.nickName)
        assertThat(user.phoneNum).isEqualTo(userUpdateReqDto.phoneNum)
        assertThat(user.password).isEqualTo(password)
    }

    @Test
    @DisplayName("유저 탈퇴 테스트")
    fun withdrawalTest() {
        // given
        userService.signUp(userSignUpDto)
        val user = userRepository.findByUserName(userSignUpDto.userName).get()
        val withdrawalUserReqDto = MockEntity.withdrawalUserReqDto(null)

        // when
        userService.withdrawal(user, withdrawalUserReqDto)
        val withdrawal = user.withdrawal!!

        // then
        assertThat(user.withdrawalStatus).isEqualTo(WAIT)
        assertThat(withdrawal.content).isNull()
        assertThat(withdrawal.reason.size).isEqualTo(3)
        assertThat(withdrawal.reason).isEqualTo(listOf(WithdrawalReason.ROW_USE, WithdrawalReason.INSUFFICIENT_CONTENT, WithdrawalReason.ETC))
    }
}
