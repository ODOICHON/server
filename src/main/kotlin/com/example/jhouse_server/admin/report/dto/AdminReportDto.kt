package com.example.jhouse_server.admin.report.dto

data class ReportSearch(
    val filter: AdminReportSearchFilter?,
    val keyword: String?
)

data class ReportAgentList(
    val agentIds: List<Long>?
)

enum class AdminReportSearchFilter(val value: String) {
    NICKNAME("닉네임")
}