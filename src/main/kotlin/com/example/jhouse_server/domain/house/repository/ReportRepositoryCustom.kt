package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.admin.report.dto.ReportSearch
import com.example.jhouse_server.domain.house.repository.dto.ReportDetailQueryDto
import com.example.jhouse_server.domain.house.repository.dto.ReportDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ReportRepositoryCustom {

    fun getReports(reportSearch: ReportSearch, pageable: Pageable): Page<ReportDto>

    fun getReportDetail(id: Long): List<ReportDetailQueryDto>
}