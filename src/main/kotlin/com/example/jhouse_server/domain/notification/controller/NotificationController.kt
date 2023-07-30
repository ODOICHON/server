package com.example.jhouse_server.domain.notification.controller

import com.example.jhouse_server.domain.notification.dto.NotificationReqDto
import com.example.jhouse_server.domain.notification.dto.SliceNotificationResDto
import com.example.jhouse_server.domain.notification.service.NotificationService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    val notificationService: NotificationService
) {

    @Auth
    @GetMapping
    fun getNotifications(
        @AuthUser user: User,
        @PageableDefault(size = 5) pageable: Pageable,
        @ModelAttribute req: NotificationReqDto
    ): ApplicationResponse<SliceNotificationResDto> {
        return ApplicationResponse.ok(notificationService.getNotifications(user, pageable, req))
    }

    @Auth
    @PutMapping("/{id}")
    fun updateNotification(
        @PathVariable("id") id: Long,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(notificationService.updateNotification(id, user))
    }
}