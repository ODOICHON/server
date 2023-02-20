package com.example.jhouse_server.global

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthCheckController {
    @GetMapping("/")
    fun healthCheck() : String {
        return "Server is Up!";
    }

    @GetMapping("/api/test")
    fun healthCheckV2() : String {
        return "test success!";
    }
}