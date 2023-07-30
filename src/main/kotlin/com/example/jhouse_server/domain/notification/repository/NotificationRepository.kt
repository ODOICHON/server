package com.example.jhouse_server.domain.notification.repository

import com.example.jhouse_server.domain.notification.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository: JpaRepository<Notification, Long>, NotificationRepositoryCustom {
}