package com.example.jhouse_server.domain.notification.service

import com.example.jhouse_server.domain.notification.dto.NotificationReqDto
import com.example.jhouse_server.domain.notification.dto.SliceNotificationResDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Pageable

interface NotificationService {

    fun getNotifications(user: User, pageable: Pageable, req: NotificationReqDto): SliceNotificationResDto

    fun updateNotification(id: Long, user: User): Long
}