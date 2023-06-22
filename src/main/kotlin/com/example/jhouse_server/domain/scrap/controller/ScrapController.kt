package com.example.jhouse_server.domain.scrap.controller

import com.example.jhouse_server.domain.scrap.service.ScrapService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/scraps")
class ScrapController(
    val scrapService: ScrapService
) {

    @Auth
    @PutMapping("/{houseId}")
    fun scrapHouse(
        @PathVariable houseId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(scrapService.scrapHouse(houseId, user))
    }

}