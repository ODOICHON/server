package com.example.jhouse_server.domain.user

import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
        val userService: UserService
) {

    @PostMapping("/api/v1/users")
    fun createUser(
            @RequestBody req : UserReqDto
    ) : ApplicationResponse<UserResDto> {
        return ApplicationResponse.ok(userService.createUser(req))
    }
}