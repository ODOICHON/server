package com.example.jhouse_server.domain.notification.repository

import com.example.jhouse_server.domain.board.entity.QBoard.board
import com.example.jhouse_server.domain.notification.dto.NotificationReqDto
import com.example.jhouse_server.domain.notification.dto.NotificationResDto
import com.example.jhouse_server.domain.notification.dto.toResDto
import com.example.jhouse_server.domain.notification.entity.Notification
import com.example.jhouse_server.domain.notification.entity.QNotification.notification
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

class NotificationRepositoryImpl(
    private val jpaQueryFactory: JPAQueryFactory
): NotificationRepositoryCustom {

    override fun findNotifications(user: User, pageable: Pageable, req: NotificationReqDto): Slice<NotificationResDto> {
        val content = jpaQueryFactory
            .selectFrom(notification)
            .join(notification.board, board).fetchJoin()
            .where(
                notification.user.eq(user),
                eqRead(req.read),
                ltId(req.id)
            )
            .orderBy(notification.id.desc())
            .limit(pageable.pageSize + 1L)
            .fetch()

        return createSliceNotification(content, pageable)
    }

    /**
     * 읽은 알림인지 아닌지 확인하는 함수
     * null : 전부
     * true : 읽음
     * false : 읽지않음
     */
    private fun eqRead(read: Boolean?): BooleanExpression? {
        return if(read == null) null
        else if(read) notification.status.eq(true)
        else notification.status.eq(false)
    }

    /**
     * 마지막 확인한 id 보다 작은 id를 가져오는 함수
     */
    private fun ltId(id: Long?): BooleanExpression? {
        return if(id == null) null
        else notification.id.lt(id)
    }

    /**
     * list를 slice로 변형하는 함수
     */
    private fun createSliceNotification(content: MutableList<Notification>, pageable: Pageable): Slice<NotificationResDto> {
        var hasNext: Boolean = false

        if(content.size > pageable.pageSize) {
            hasNext = true
            content.removeAt(pageable.pageSize)
        }

        return SliceImpl(content.map { toResDto(it) }, pageable, hasNext)
    }
}