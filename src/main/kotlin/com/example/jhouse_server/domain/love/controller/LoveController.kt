package com.example.jhouse_server.domain.love.controller

import com.example.jhouse_server.domain.love.service.LoveService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/loves")
class LoveController(
    val loveService: LoveService
) {

    @Auth
    @PutMapping("/{boardId}")
    fun loveBoard(
        @PathVariable boardId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(loveService.loveBoard(boardId, user))
    }

    @Auth
    @DeleteMapping("/{boardId}")
    fun hateBoard(
        @PathVariable boardId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        loveService.hateBoard(boardId, user)
        return ApplicationResponse.ok()
    }
    @Auth
    @GetMapping("/{boardId}")
    fun isLovedBoard(
        @PathVariable boardId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Boolean> {
        return ApplicationResponse.ok(loveService.isLovedBoard(boardId, user))
    }
}