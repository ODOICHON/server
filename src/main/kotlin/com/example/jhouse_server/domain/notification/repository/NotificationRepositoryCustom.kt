package com.example.jhouse_server.domain.notification.repository

import com.example.jhouse_server.domain.notification.dto.NotificationReqDto
import com.example.jhouse_server.domain.notification.dto.NotificationResDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface NotificationRepositoryCustom {

    fun findNotifications(user: User, pageable: Pageable, req: NotificationReqDto): Slice<NotificationResDto>
}