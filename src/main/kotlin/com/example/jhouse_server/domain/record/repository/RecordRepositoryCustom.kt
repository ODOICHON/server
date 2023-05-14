package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.dto.RecordHotThumbnailResDto
import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordThumbnailResDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface RecordRepositoryCustom {

    fun findHotRecords(weekAgo: LocalDateTime): List<RecordHotThumbnailResDto>

    fun findRecordsByUser(user: User, pageable: Pageable): Page<RecordThumbnailResDto>

    fun findRecords(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto>
}