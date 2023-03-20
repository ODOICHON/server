package com.example.jhouse_server.admin.user.controller

import com.example.jhouse_server.domain.user.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/admin")
@Controller
class AdminUserController (
        var userService: UserService
        ){

        // 메인 페이지 - 로그인 화면
        @GetMapping
        fun getSignIn() : String {
                return "login"
        }


}