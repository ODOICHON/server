package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface RecordRepositoryCustom {

    fun findHotRecords(weekAgo: LocalDateTime): List<RecordHotThumbnailResDto>

    fun findRecords(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto>

    fun findRevieweeRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): Page<RecordThumbnailResDto>

    fun findReviewerRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): Page<RecordThumbnailResDto>
}