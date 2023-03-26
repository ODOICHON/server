package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.*
import com.example.jhouse_server.domain.user.repository.UserJoinPathRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode.*
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.util.RedisUtil
import com.example.jhouse_server.global.util.SmsUtil
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
@Transactional(readOnly = true)
class UserServiceImpl (
        val userRepository: UserRepository,
        val userJoinPathRepository: UserJoinPathRepository,
        val tokenProvider: TokenProvider,
        val redisUtil: RedisUtil,
        val smsUtil: SmsUtil
): UserService {
    private val AUTHORIZATION_HEADER: String = "Authorization"

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
        val age: Age = Age.getAge(userSignUpReqDto.age)!!
        val joinPaths: MutableList<JoinPath> = mutableListOf()
        for(joinPath in userSignUpReqDto.joinPaths) {
            joinPaths.add(JoinPath.getJoinPath(joinPath)!!)
        }

        val user = User(userSignUpReqDto.email, encodePassword(userSignUpReqDto.password),
                userSignUpReqDto.nickName, userSignUpReqDto.phoneNum,
                Authority.USER, age)
        userRepository.save(user)

        for(joinPath in joinPaths) {
            saveUserJoinPath(joinPath, user)
        }
    }

    override fun signIn(userSignInReqDto: UserSignInReqDto): TokenDto {
        val user = userRepository.findByEmail(userSignInReqDto.email)
                .orElseThrow{ ApplicationException(DONT_EXIST_EMAIL) }
        if (user.password != encodePassword(userSignInReqDto.password)) {
            throw ApplicationException(DONT_MATCH_PASSWORD)
        }

        val tokenResponse = tokenProvider.createTokenResponse(user)
        redisUtil.setValuesExpired(tokenResponse.accessToken, tokenResponse.refreshToken)

        return tokenResponse
    }

    override fun reissue(tokenDto: TokenDto): TokenDto {
        tokenProvider.validateToken(tokenDto.refreshToken)

        val email = tokenProvider.getSubject(tokenDto.refreshToken)
        val refreshToken: String? = redisUtil.getValues(tokenDto.accessToken)

        if (refreshToken.isNullOrEmpty()) {
            throw ApplicationException(ALREADY_LOGOUT)
        }
        if (refreshToken != tokenDto.refreshToken) {
            throw ApplicationException(DONT_MATCH_WITH_TOKEN)
        }
        redisUtil.deleteValues(tokenDto.accessToken)
        val user = userRepository.findByEmail(email)
                .orElseThrow{ ApplicationException(DONT_EXIST_EMAIL) }

        val updateTokenResponse = tokenProvider.createTokenResponse(user)
        redisUtil.setValuesExpired(updateTokenResponse.accessToken, updateTokenResponse.refreshToken)

        return updateTokenResponse
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
        val encodePassword = encodePassword(password)
        if (user.password == encodePassword) {
            throw ApplicationException(SAME_PASSWORD)
        }

        user.updatePassword(encodePassword)
    }

    private fun createCode(): String {
        val random: Random = Random()

        return String.format("%04d", random.nextInt(10000))
    }

    private fun encodePassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.reset()
        messageDigest.update(password.toByteArray(StandardCharsets.UTF_8))

        return String.format("%0128x", BigInteger(1, messageDigest.digest()))
    }

    private fun saveUserJoinPath(joinPath: JoinPath, user: User) {
        val userJoinPath = UserJoinPath(joinPath, user)
        userJoinPathRepository.save(userJoinPath)
    }
}