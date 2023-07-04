package com.example.jhouse_server.admin.user.service

import com.example.jhouse_server.admin.user.dto.join.AdminJoinAgentList
import com.example.jhouse_server.admin.user.dto.withdrawal.AdminUserWithdrawalSearch
import com.example.jhouse_server.admin.user.dto.withdrawal.AdminWithdrawalList
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.repository.AgentRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminUserService(
        var userRepository: UserRepository,
        var agentRepository: AgentRepository
) {

    fun agentJoin(adminJoinAgentList: AdminJoinAgentList) {
        val agentReq = adminJoinAgentList.joinAgentIds?.let { agentRepository.findByIds(it) }
        agentReq?.forEach { a -> a.updateStatus(AgentStatus.APPROVE) }
    }

    fun withdrawalUser(adminWithdrawalList: AdminWithdrawalList) {
        val findUsers = adminWithdrawalList.withdrawalIds?.let { userRepository.findByIds(it) }
        findUsers?.forEach { u -> u.withdrawalUser() }
    }

}