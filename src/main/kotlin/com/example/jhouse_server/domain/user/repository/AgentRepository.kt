package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.WithdrawalStatus
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AgentRepository: JpaRepository<Agent, Long> {

    fun countByStatus(status: AgentStatus): Long

    @Query("select a from Agent a where a.id in (:ids)")
    fun findByIds(@Param("ids") ids: List<Long>): List<Agent>


}