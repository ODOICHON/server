package com.example.jhouse_server.domain.user.controller

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.Authority.USER
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
        val userService: UserService
) {

    @GetMapping("/check/email/{email}")
    fun emailCheck(
            @PathVariable("email") email: String
    ): ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(userService.checkEmail(email))
    }

    @GetMapping("/check/nick-name/{nick-name}")
    fun nickNameCheck(
            @PathVariable("nick-name") nickName: String
    ): ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(userService.checkNickName(nickName))
    }

    @PostMapping("/send/sms")
    fun sendSms(
            @RequestParam("phone_num") phoneNum: String
    ): ApplicationResponse<Nothing> {
        userService.sendSmsCode(phoneNum)

        return ApplicationResponse.ok()
    }

    @PostMapping("/check/sms")
    fun checkSms(
            @RequestBody checkSmsReqDto: CheckSmsReqDto
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
            @RequestBody userSignInReqDto: UserSignInReqDto
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
    @PutMapping("/update/nick-name/{nick-name}")
    fun updateNickName(
            @AuthUser user: User,
            @PathVariable("nick-name") nickName: String
    ): ApplicationResponse<Nothing> {
        userService.updateNickName(user, nickName)

        return ApplicationResponse.ok()
    }

    @Auth
    @PutMapping("/update/password/{password}")
    fun updatePassword(
            @AuthUser user: User,
            @PathVariable("password") password: String
    ): ApplicationResponse<Nothing> {
        userService.updatePassword(user, password)

        return ApplicationResponse.ok()
    }
}