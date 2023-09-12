package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.admin.house.dto.AdminDealDto
import com.example.jhouse_server.admin.house.dto.AdminHouseSearch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DealRepositoryCustom {
    fun getReviewHouseListWithPaging(adminHouseSearch: AdminHouseSearch, pageable: Pageable): Page<AdminDealDto>
}