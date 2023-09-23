package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.jwt.TokenDto

interface UserService {
    fun findUserById(userId : Long) : UserResDto

    fun checkUserName(userName: String): Boolean

    fun checkNickName(nickName: String): Boolean

    fun sendSmsCode(phoneNum: String)

    fun checkSmsCode(checkSmsReqDto: CheckSmsReqDto): Boolean

    fun signUp(userSignUpReqDto: UserSignUpReqDto)

    fun signIn(userSignInReqDto: UserSignInReqDto): TokenDto

    fun reissue(accessToken: String, refreshToken: String): TokenDto

    fun logout(token: String)

    fun update(user: User, userUpdateReqDto: UserUpdateReqDto)

    fun updateNickName(user: User, nickName: String)

    fun updatePassword(user: User, password: String)

    fun withdrawal(user: User, withdrawalUserReqDto: WithdrawalUserReqDto)
    fun sendEmailCode(email: String)
    fun checkEmailCode(checkEmailReqDto: CheckEmailReqDto): Boolean
}