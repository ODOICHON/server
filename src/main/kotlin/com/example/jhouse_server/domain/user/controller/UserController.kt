package com.example.jhouse_server.domain.user.controller

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.Authority.USER
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.exception.ErrorCode.INVALID_VALUE_EXCEPTION
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.regex.Pattern

@RestController
@RequestMapping("/api/v1/users")
class UserController(
        val userService: UserService
) {
    @Auth
    @GetMapping
    fun getUser(
            @AuthUser user: User
    ): ApplicationResponse<UserResDto> {
        return ApplicationResponse.ok(userService.findUserById(user.id))
    }

    @PostMapping("/check/email")
    fun emailCheck(
            @Validated @RequestBody emailReqDto: EmailReqDto
    ): ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(userService.checkEmail(emailReqDto.email))
    }

    @PostMapping("/check/nick-name")
    fun nickNameCheck(
            @Validated @RequestBody nickNameReqDto: NickNameReqDto
    ): ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(userService.checkNickName(nickNameReqDto.nickName))
    }

    @PostMapping("/send/sms")
    fun sendSms(
            @Validated @RequestBody phoneNumReqDto: PhoneNumReqDto
    ): ApplicationResponse<Nothing> {
        userService.sendSmsCode(phoneNumReqDto.phoneNum)

        return ApplicationResponse.ok()
    }

    @PostMapping("/check/sms")
    fun checkSms(
            @Validated @RequestBody checkSmsReqDto: CheckSmsReqDto
    ): ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(userService.checkSmsCode(checkSmsReqDto))
    }

    @PostMapping("/sign-up")
    fun signUp(
            @Validated @RequestBody userSignUpReqDto: UserSignUpReqDto
    ): ApplicationResponse<Nothing> {
        userService.signUp(userSignUpReqDto)

        return ApplicationResponse.ok()
    }

    @PostMapping("/sign-in")
    fun signIn(
            @Validated @RequestBody userSignInReqDto: UserSignInReqDto
    ): ApplicationResponse<TokenDto> {
        return ApplicationResponse.ok(userService.signIn(userSignInReqDto))
    }

    @PostMapping("/reissue")
    fun reissue(
            @RequestBody tokenDto: TokenDto
    ): ApplicationResponse<TokenDto> {
        return ApplicationResponse.ok(userService.reissue(tokenDto))
    }

    @Auth
    @PostMapping("/logout")
    fun logout(
            @AuthUser user: User
    ): ApplicationResponse<Nothing> {
        userService.logout(user.email)

        return ApplicationResponse.ok()
    }

    @Auth
    @PutMapping("/update/nick-name")
    fun updateNickName(
            @AuthUser user: User,
            @Validated @RequestBody nickNameReqDto: NickNameReqDto
    ): ApplicationResponse<Nothing> {
        userService.updateNickName(user, nickNameReqDto.nickName)

        return ApplicationResponse.ok()
    }

    @Auth
    @PutMapping("/update/password")
    fun updatePassword(
            @AuthUser user: User,
            @Validated @RequestBody passwordReqDto: PasswordReqDto
    ): ApplicationResponse<Nothing> {
        userService.updatePassword(user, passwordReqDto.password)

        return ApplicationResponse.ok()
    }
}