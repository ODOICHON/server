package com.example.jhouse_server.domain.user.service.common

import com.example.jhouse_server.domain.user.UserSignUpReqDto
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

@Component
class UserServiceCommonMethod(
    val userRepository: UserRepository,
    val userJoinPathRepository: UserJoinPathRepository
) {

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
}