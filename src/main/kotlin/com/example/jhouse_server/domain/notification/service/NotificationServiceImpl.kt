package com.example.jhouse_server.domain.notification.service

import com.example.jhouse_server.domain.notification.dto.NotificationReqDto
import com.example.jhouse_server.domain.notification.dto.SliceNotificationResDto
import com.example.jhouse_server.domain.notification.repository.NotificationRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode.UNAUTHORIZED_EXCEPTION
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NotificationServiceImpl(
    /**
     * =============================================================================================
     * DI for Repository
     * =============================================================================================
     */
    val notificationRepository: NotificationRepository
): NotificationService {
    /**
     * =============================================================================================
     * 알림 조회
     * =============================================================================================
     */
    override fun getNotifications(user: User, pageable: Pageable, req: NotificationReqDto): SliceNotificationResDto {
        val notifications = notificationRepository.findNotifications(user, pageable, req)
        var nextId: Long? = null
        if(notifications.content.isNotEmpty()) nextId = notifications.content[notifications.content.size - 1].id
        return SliceNotificationResDto(nextId, notifications)
    }
    /**
     * =============================================================================================
     * 알림 수신
     * =============================================================================================
     */
    @Transactional
    override fun updateNotification(id: Long, user: User): Long {
        val notification = notificationRepository.findByIdOrThrow(id)
        if(notification.user != user) throw ApplicationException(UNAUTHORIZED_EXCEPTION)
        notification.updateStatus(true)
        return notification.id
    }
}