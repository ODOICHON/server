package com.example.jhouse_server.admin.user.controller

import com.example.jhouse_server.domain.user.service.UserService
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminUserController (
        var userService: UserService
        ){

        // 메인 페이지 - 로그인 화면
        @GetMapping("/admin")
        fun getSignIn() : String {
                return "login"
        }



}