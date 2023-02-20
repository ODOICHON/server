package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.exception.ErrorCode.*
import com.example.jhouse_server.global.findByIdOrThrow
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.RedisUtil
import com.example.jhouse_server.global.util.SmsUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*

@Service
@Transactional(readOnly = true)
class UserServiceImpl (
        val userRepository: UserRepository,
        val tokenProvider: TokenProvider,
        val redisUtil: RedisUtil,
        val smsUtil: SmsUtil
): UserService {

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
        redisUtil.setValues(phoneNum, code)
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
        val user = User(userSignUpReqDto.email, encodePassword(userSignUpReqDto.password),
                userSignUpReqDto.nickName, userSignUpReqDto.phoneNum,
                Authority.USER)
        userRepository.save(user)
    }

    override fun signIn(userSignInReqDto: UserSignInReqDto): TokenDto {
        val user = userRepository.findByEmail(userSignInReqDto.email)
                .orElseThrow{ ApplicationException(DONT_EXIST_EMAIL) }
        if (user.password != encodePassword(userSignInReqDto.password)) {
            throw ApplicationException(DONT_MATCH_PASSWORD)
        }

        val tokenResponse = tokenProvider.createTokenResponse(user)
        redisUtil.setValues(user.email, tokenResponse.refreshToken)

        return tokenResponse
    }

    override fun reissue(tokenDto: TokenDto): TokenDto {
        val accessToken = tokenProvider.resolveToken(tokenDto.accessToken).toString()

        tokenProvider.validateToken(accessToken)
        tokenProvider.validateToken(tokenDto.refreshToken)

        val email = tokenProvider.getSubject(accessToken)
        val refreshToken: String? = redisUtil.getValues(email)

        if (refreshToken.isNullOrEmpty()) {
            throw ApplicationException(ALREADY_LOGOUT)
        }
        if (refreshToken != tokenDto.refreshToken) {
            throw ApplicationException(DONT_MATCH_WITH_TOKEN)
        }
        val user = userRepository.findByEmail(email)
                .orElseThrow{ ApplicationException(DONT_EXIST_EMAIL) }

        val updateTokenResponse = tokenProvider.createTokenResponse(user)
        redisUtil.setValues(user.email, updateTokenResponse.refreshToken)

        return updateTokenResponse
    }

    override fun logout(user: User) {
        redisUtil.deleteValues(user.email)
    }

    private fun createCode(): String {
        val random: Random = Random()

        return String.format("%06d", random.nextInt(1000000))
    }

    private fun encodePassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.reset()
        messageDigest.update(password.toByteArray(StandardCharsets.UTF_8))

        return String.format("%0128x", BigInteger(1, messageDigest.digest()))
    }
}