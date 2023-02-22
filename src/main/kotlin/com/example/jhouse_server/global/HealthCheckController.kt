package com.example.jhouse_server.global

import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.Authority.ADMIN
import com.example.jhouse_server.domain.user.entity.Authority.USER
import com.example.jhouse_server.global.annotation.Auth
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/api")
    fun healthCheck() : String {
        return "Server is Up!";
    }

    @GetMapping("/api/test")
    fun healthCheckV2() : String {
        return "test success!";
    }

    @GetMapping("/api/please")
    fun healthCheckV3() : String {
        return "제발좀되라!!";
    }

    @Auth
    @GetMapping("/api/auth/user")
    fun healthCheckV4() : String {
        return "못들어오지~?"
    }

    @Auth(ADMIN)
    @GetMapping("/api/auth/admin")
    fun healthCheckV5() : String {
        return "관리자만 오시오"
    }
}