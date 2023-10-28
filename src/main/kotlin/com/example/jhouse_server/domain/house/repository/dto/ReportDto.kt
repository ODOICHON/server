package com.example.jhouse_server.domain.house.repository.dto

import com.example.jhouse_server.domain.house.entity.ReportType
import com.example.jhouse_server.domain.user.entity.User
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

/**
 * =============================================================================================
 * RESPONSE DTO
 * =============================================================================================
 */
data class ReportDto (
    val id: Long,
    val nickname: String,
    val agentName: String,
    val phoneNum: String,
    val reportCount: Long,
    val suspension: Boolean
)
/**
 * =============================================================================================
 * QUERY RESULT VO
 * =============================================================================================
 */
data class ReportQueryDto @QueryProjection constructor(
    val user: User,
    val reportCount: Long
)
data class ReportDetailQueryDto @QueryProjection constructor(
    val date: LocalDateTime,
    val id: Long,
    val nickname: String,
    val reportType: ReportType
)
