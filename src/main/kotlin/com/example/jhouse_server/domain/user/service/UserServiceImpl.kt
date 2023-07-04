package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.*
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.common.UserServiceCommonMethod
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode.*
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.RedisUtil
import com.example.jhouse_server.global.util.SmsUtil
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserServiceImpl (
        val userRepository: UserRepository,
        val tokenProvider: TokenProvider,
        val redisUtil: RedisUtil,
        val smsUtil: SmsUtil,
        val userServiceCommonMethod: UserServiceCommonMethod
): UserService {
    private val SMS_CODE_EXPIRE_TIME: Long = 60 * 3  //3분

    override fun findUserById(userId: Long): UserResDto {
        val findUser = userRepository.findByIdOrThrow(userId)

        return toDto(findUser)
    }

    override fun checkEmail(email: String): Boolean {
        return !userRepository.existsByEmail(email)
    }

    override fun checkNickName(nickName: String): Boolean {
        return !userRepository.existsByNickName(nickName)
    }

    override fun sendSmsCode(phoneNum: String) {
        if (userRepository.existsByPhoneNum(phoneNum)) {
            throw ApplicationException(EXIST_PHONE_NUM)
        }
        val code = createCode()
        smsUtil.sendMessage(phoneNum, code)
        redisUtil.setValuesExpired(phoneNum, code, SMS_CODE_EXPIRE_TIME)
    }

    override fun checkSmsCode(checkSmsReqDto: CheckSmsReqDto): Boolean {
        val savedCode: String = redisUtil.getValues(checkSmsReqDto.phoneNum).toString()

        if (savedCode == checkSmsReqDto.code) {
            redisUtil.deleteValues(checkSmsReqDto.phoneNum)
            return true
        }
        return false
    }

    @Transactional
    override fun signUp(userSignUpReqDto: UserSignUpReqDto) {
        userServiceCommonMethod.validateDuplicate(userSignUpReqDto.email, userSignUpReqDto.nickName, userSignUpReqDto.phoneNum)

        val age: Age = Age.getAge(userSignUpReqDto.age)!!
        val joinPaths: MutableList<JoinPath> = mutableListOf()
        for(joinPath in userSignUpReqDto.joinPaths) {
            joinPaths.add(JoinPath.getJoinPath(joinPath)!!)
        }

        val user = User(userSignUpReqDto.email, userServiceCommonMethod.encodePassword(userSignUpReqDto.password),
                userSignUpReqDto.nickName, userSignUpReqDto.phoneNum,
                Authority.USER, age, UserType.NONE, null)
        userRepository.save(user)

        for(joinPath in joinPaths) {
            userServiceCommonMethod.saveUserJoinPath(joinPath, user)
        }
    }

    override fun signIn(userSignInReqDto: UserSignInReqDto): TokenDto {
        val user = userRepository.findByEmail(userSignInReqDto.email)
                .orElseThrow{ ApplicationException(DONT_EXIST_EMAIL) }
        if (user.password != userServiceCommonMethod.encodePassword(userSignInReqDto.password)) {
            throw ApplicationException(DONT_MATCH_PASSWORD)
        }

        return tokenProvider.createTokenResponse(user)
    }

    override fun reissue(bearerToken: String, refreshToken: String): TokenDto {tokenProvider.validateToken(refreshToken, false)

        val accessToken = tokenProvider.resolveToken(bearerToken).toString()
        val email = tokenProvider.getSubject(accessToken)
        val user = userRepository.findByEmail(email)
                .orElseThrow{ ApplicationException(DONT_EXIST_EMAIL) }
        return tokenProvider.createTokenResponse(user)
    }

    override fun logout(token: String) {
        redisUtil.deleteValues(token)
    }

    @Transactional
    override fun updateNickName(user: User, nickName: String) {
        if (userRepository.existsByNickName(nickName)) {
            throw ApplicationException(EXIST_NICK_NAME)
        }

        user.updateNickName(nickName)
    }

    @Transactional
    override fun updatePassword(user: User, password: String) {
        val encodePassword = userServiceCommonMethod.encodePassword(password)
        if (user.password == encodePassword) {
            throw ApplicationException(SAME_PASSWORD)
        }

        user.updatePassword(encodePassword)
    }

    @Transactional
    override fun withdrawal(user: User) {
        user.updateWithdrawalStatus(WAIT)
    }

    private fun createCode(): String {
        val random: Random = Random()

        return String.format("%04d", random.nextInt(10000))
    }
}