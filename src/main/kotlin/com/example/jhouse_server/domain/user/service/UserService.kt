package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.CheckSmsReqDto
import com.example.jhouse_server.domain.user.UserResDto
import com.example.jhouse_server.domain.user.UserSignInReqDto
import com.example.jhouse_server.domain.user.UserSignUpReqDto
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.jwt.TokenDto

interface UserService {
    fun findUserById(userId : Long) : UserResDto

    fun checkEmail(email: String): Boolean

    fun checkNickName(nickName: String): Boolean

    fun sendSmsCode(phoneNum: String)

    fun checkSmsCode(checkSmsReqDto: CheckSmsReqDto): Boolean

    fun signUp(userSignUpReqDto: UserSignUpReqDto)

    fun signIn(userSignInReqDto: UserSignInReqDto): TokenDto

    fun reissue(tokenDto: TokenDto): TokenDto

    fun logout(email: String)

    fun updateNickName(user: User, nickName: String)

    fun updatePassword(user: User, password: String)
}