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
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/notifications")
class NotificationController(
    /**
     * =============================================================================================
     * DI for Service
     * =============================================================================================
     */
    val notificationService: NotificationService
) {
    /**
     * =============================================================================================
     * 알림 조회
     *
     * @param user
     * @param pageable
     * @param req
     * =============================================================================================
     */
    @Auth
    @GetMapping
    fun getNotifications(
        @AuthUser user: User,
        @PageableDefault(size = 5) pageable: Pageable,
        @ModelAttribute req: NotificationReqDto
    ): ApplicationResponse<SliceNotificationResDto> {
        return ApplicationResponse.ok(notificationService.getNotifications(user, pageable, req))
    }

    /**
     * =============================================================================================
     * 알림 수신
     *
     * @param id
     * @param user
     * =============================================================================================
     */
    @Auth
    @PutMapping("/{id}")
    fun updateNotification(
        @PathVariable("id") id: Long,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(notificationService.updateNotification(id, user))
    }
}