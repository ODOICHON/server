package com.example.jhouse_server.admin.user.service


import com.example.jhouse_server.admin.user.dto.AdminJoinAgentList
import com.example.jhouse_server.admin.user.dto.AdminUserList
import com.example.jhouse_server.admin.user.dto.AdminUserWithdrawalSearch
import com.example.jhouse_server.admin.user.dto.AdminWithdrawalList
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.repository.AgentRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminUserService(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
        var userRepository: UserRepository,
        var agentRepository: AgentRepository
) {
    /**
     * =============================================================================================
     *  공인중개사 가입 승인
     * =============================================================================================
     * */
    fun agentJoin(adminJoinAgentList: AdminJoinAgentList) {
        val agentReq = adminJoinAgentList.joinAgentIds?.let { agentRepository.findByIds(it) }
        agentReq?.forEach { a -> a.updateStatus(AgentStatus.APPROVE) }
    }
    /**
     * =============================================================================================
     *  사용자 회원 탈퇴
     * =============================================================================================
     * */
    fun withdrawalUser(adminWithdrawalList: AdminWithdrawalList) {
        val findUsers = adminWithdrawalList.withdrawalIds?.let { userRepository.findByIds(it) }
        findUsers?.forEach { u -> u.withdrawalUser() }
    }

    fun getUserWithSearchForm(
        adminUserSearch: AdminUserWithdrawalSearch,
        pageable: Pageable
    ): Page<AdminUserList> {
        return userRepository.getUserWithSearchForm(adminUserSearch, pageable)
    }

}