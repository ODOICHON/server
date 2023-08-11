package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse
import com.example.jhouse_server.admin.user.dto.AdminAgentSearch
import com.example.jhouse_server.admin.user.dto.AdminUserWithdrawalSearch
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.agent.Agent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable


interface UserRepositoryCustom {
    fun getAnalysisAgeResult() : List<Double>

    fun getAnalysisJoinPathResults() : List<AnalysisJoinPathResponse>

    fun getWaitingAgentResult(adminAgentSearch: AdminAgentSearch, pageable: Pageable): Page<Agent>

    fun getAgentWithdrawalReqResult(adminAgentSearch: AdminAgentSearch, pageable: Pageable): Page<Agent>

    fun getUserWithdrawalReqResult(adminUserWithdrawalSearch: AdminUserWithdrawalSearch, pageable: Pageable): Page<User>
}