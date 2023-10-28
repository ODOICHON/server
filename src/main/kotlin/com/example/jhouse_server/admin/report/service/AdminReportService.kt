package com.example.jhouse_server.admin.report.service

import com.example.jhouse_server.admin.report.dto.ReportAgentList
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Service
class AdminReportService(
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
    val userRepository: UserRepository
) {

    /**
     * =============================================================================================
     *  계정 정지
     * =============================================================================================
     * */
    @Transactional
    fun suspensionUser(agentList: ReportAgentList, redirectAttributes: RedirectAttributes) {
        val findUsers = agentList.agentIds?.let { userRepository.findByIds(it) }
        findUsers?.forEach { u ->
            if(u.suspension) redirectAttributes.addAttribute("already_suspension", "이미 정지처리 된 계정이 포함되어 있습니다.")
        }
        if (redirectAttributes.containsAttribute("already_suspension")) return
        findUsers?.forEach { u -> u.updateSuspension(true) }
    }
    /**
     * =============================================================================================
     *  계정 정지 해제
     * =============================================================================================
     * */
    @Transactional
    fun cancelSuspensionUser(agentList: ReportAgentList, redirectAttributes: RedirectAttributes) {
        val findUsers = agentList.agentIds?.let { userRepository.findByIds(it) }
        findUsers?.forEach { u ->
            if(!u.suspension) redirectAttributes.addAttribute("already_cancel", "이미 정지해제된 계정이 포함되어 있습니다.")
        }
        if (redirectAttributes.containsAttribute("already_cancel")) return
        findUsers?.forEach { u -> u.updateSuspension(false) }
    }

}