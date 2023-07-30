package com.example.jhouse_server.domain.user.service.common

import com.example.jhouse_server.domain.user.entity.JoinPath
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserJoinPath
import com.example.jhouse_server.domain.user.repository.UserJoinPathRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.regex.Pattern

@Component
class UserServiceCommonMethod(
    val userRepository: UserRepository,
    val userJoinPathRepository: UserJoinPathRepository
) {

    private val NICK_NAME_PATTERN = "^(?=.*[a-zA-Z0-9가-힣])[A-Za-z0-9가-힣]{1,20}\$"

    private val PW_PATTERN = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*.?])[A-Za-z0-9!@#\$%^&*.?]{8,16}\$"

    private val PHONE_NUM_PATTERN = "^01(?:0|1|[6-9])[0-9]{7,8}"

    fun encodePassword(password: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-512")
        messageDigest.reset()
        messageDigest.update(password.toByteArray(StandardCharsets.UTF_8))

        return String.format("%0128x", BigInteger(1, messageDigest.digest()))
    }

    fun saveUserJoinPath(joinPath: JoinPath, user: User) {
        val userJoinPath = UserJoinPath(joinPath, user)
        userJoinPathRepository.save(userJoinPath)
    }

    fun validateDuplicate(email: String, nickName: String, phoneNum: String) {
        if(userRepository.existsByEmail(email)) {
            throw ApplicationException(ErrorCode.EXIST_EMAIL)
        }
        if(userRepository.existsByNickName(nickName)) {
            throw ApplicationException(ErrorCode.EXIST_NICK_NAME)
        }
        if(userRepository.existsByPhoneNum(phoneNum)) {
            throw ApplicationException(ErrorCode.EXIST_PHONE_NUM)
        }
    }

    fun validateNickName(nickName: String) {
        if (!Pattern.matches(NICK_NAME_PATTERN, nickName)) {
            throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
        if (userRepository.existsByNickName(nickName)) {
            throw ApplicationException(ErrorCode.EXIST_NICK_NAME)
        }
    }

    fun validatePassword(password: String) {
        if (!Pattern.matches(PW_PATTERN, password)) {
            throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
    }

    fun validatePhoneNum(phoneNum: String) {
        if (!Pattern.matches(PHONE_NUM_PATTERN, phoneNum)) {
            throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
        }
        if (userRepository.existsByPhoneNum(phoneNum)) {
            throw ApplicationException(ErrorCode.EXIST_PHONE_NUM)
        }
    }
}