package com.example.jhouse_server.domain.notification.dto

import com.example.jhouse_server.domain.notification.entity.Notification
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.domain.Slice

data class NotificationReqDto(
    val read: Boolean?,
    val id: Long?
)

data class NotificationResDto(
    @JsonProperty("nick_name")
    val nickName: String,
    @JsonProperty("board_id")
    val boardId: Long,
    @JsonProperty("board_title")
    val boardTitle: String,
    val status: Boolean,
    val comment: String,
    @JsonProperty("comment_user")
    val commentUser: String,
    @JsonProperty("notification_id")
    val id: Long
)

data class SliceNotificationResDto(
    val nextId: Long?,
    val notifications: Slice<NotificationResDto>
)

fun toResDto(notification: Notification): NotificationResDto {
    return NotificationResDto(notification.user.nickName, notification.board.id, notification.board.title,
           notification.status, notification.comment, notification.commentUser, notification.id)
}