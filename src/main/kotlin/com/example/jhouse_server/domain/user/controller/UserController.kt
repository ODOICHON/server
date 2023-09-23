package com.example.jhouse_server.domain.user.controller

import com.example.jhouse_server.domain.user.*
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.jwt.TokenDto
import com.example.jhouse_server.global.jwt.TokenProvider
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.http.ResponseCookie
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/v1/users")
class UserController(
        val userService: UserService,
        val tokenProvider: TokenProvider
) {
    private val COOKIE_NAME: String = "RefreshToken"
    private val COOKIE_EXPIRE: Long = 60 * 60 * 24 * 7

    @Auth
    @GetMapping
    fun getUser(
            @AuthUser user: User
    ): ApplicationResponse<UserResDto> {
        return ApplicationResponse.ok(userService.findUserById(user.id))
    }

    @PostMapping("/check/user-name")
    fun userNameCheck(
            @Validated @RequestBody userNameReqDto: UserNameReqDto
    ): ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(userService.checkUserName(userNameReqDto.userName))
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
            @Validated @RequestBody userSignInReqDto: UserSignInReqDto,
            request: HttpServletRequest,
            response: HttpServletResponse
    ): ApplicationResponse<TokenDto> {
        val tokenDto: TokenDto = userService.signIn(userSignInReqDto)
        setRefreshToken(request, response, COOKIE_EXPIRE)

        return ApplicationResponse.ok(tokenDto)
    }

    @PostMapping("/reissue")
    fun reissue(
        request: HttpServletRequest,
        response: HttpServletResponse,
        @RequestBody tokenDto: TokenDto,
        @CookieValue(name = "RefreshToken", required = false, defaultValue = "") refreshToken: String
    ): ApplicationResponse<TokenDto> {
        val updateTokenDto: TokenDto = userService.reissue(tokenDto.accessToken, refreshToken)
        setRefreshToken(request, response, COOKIE_EXPIRE)

        return ApplicationResponse.ok(updateTokenDto)
    }

    @Auth
    @PostMapping("/logout")
    fun logout(
            @AuthUser user: User,
            request: HttpServletRequest,
            response: HttpServletResponse
    ): ApplicationResponse<Nothing> {
        setRefreshToken(request, response, 0)

        return ApplicationResponse.ok()
    }

    @Auth
    @PutMapping
    fun update(
        @AuthUser user: User,
        @Validated @RequestBody userUpdateReqDto: UserUpdateReqDto
    ): ApplicationResponse<Nothing> {
        userService.update(user, userUpdateReqDto)

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

    @Auth
    @PostMapping("/withdrawal")
    fun withdrawal(
            @AuthUser user: User,
            @Validated @RequestBody withdrawalUserReqDto: WithdrawalUserReqDto
    ): ApplicationResponse<Nothing> {
        userService.withdrawal(user, withdrawalUserReqDto)

        return ApplicationResponse.ok()
    }

    private fun setRefreshToken(request: HttpServletRequest, response: HttpServletResponse, expireTime: Long) {
        val refreshToken: String = tokenProvider.createRefreshToken()
        val cookie = ResponseCookie.from(COOKIE_NAME, refreshToken)
            .domain(request.serverName)
            .maxAge(expireTime)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .build()
        response.setHeader("Set-Cookie", cookie.toString())
    }
}