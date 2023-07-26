package com.example.jhouse_server.admin.user.controller

import com.example.jhouse_server.admin.user.dto.AdminAgentSearch
import com.example.jhouse_server.admin.user.dto.AdminJoinAgentList
import com.example.jhouse_server.admin.user.dto.AgentSearchFilter
import com.example.jhouse_server.admin.user.dto.AdminUserWithdrawalSearch
import com.example.jhouse_server.admin.user.dto.AdminWithdrawalList
import com.example.jhouse_server.admin.user.dto.UserSearchFilter
import com.example.jhouse_server.admin.user.service.AdminUserService
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.UserType.*
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.repository.AgentRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/user")
class AdminUserController(
    val userRepository: UserRepository,
    val agentRepository: AgentRepository,
    val adminUserService: AdminUserService
) {


    // 공인중계사 가입요청 리스트
    @GetMapping("/join")
    fun getAgentList(@ModelAttribute("searchForm") adminAgentSearch: AdminAgentSearch,
                     @ModelAttribute("joinList") adminJoinAgentList: AdminJoinAgentList,
                     model: Model,
                     @PageableDefault(size=10, page=0) pageable: Pageable): String{

        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        val total = agentRepository.countByStatus(AgentStatus.WAIT)
        model.addAttribute("total", total)
        model.addAttribute("agentList", userRepository.getWaitingAgentResult(adminAgentSearch, pageable))
        model.addAttribute("filterList", AgentSearchFilter.values())
        return "user/agentJoin"

    }

    @PostMapping("/join")
    fun agentJoin(@ModelAttribute("joinList") adminJoinAgentList: AdminJoinAgentList): String {
        adminUserService.agentJoin(adminJoinAgentList)
        return "redirect:/admin/user/join"
    }

    private fun setModel(userType: UserType, model: Model, pageable: Pageable, filterList: List<Any>) {
        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        val total = userRepository.countByWithdrawalStatusAndUserType(WAIT, userType)
        model.addAttribute("total", total)
        model.addAttribute("filterList", filterList)
    }

    // 탈퇴요청 공인중개사 리스트
    @GetMapping("/withdrawal/agent")
    fun getDeleteReqAgentList(@ModelAttribute("searchForm") adminAgentSearch: AdminAgentSearch
                              , @ModelAttribute("withdrawalList") adminWithdrawalList: AdminWithdrawalList
                              , model: Model
                              , @PageableDefault(size=10, page=0) pageable: Pageable): String{
        setModel(AGENT, model, pageable, AgentSearchFilter.values().toList())
        model.addAttribute("agentList", userRepository.getAgentWithdrawalReqResult(adminAgentSearch, pageable))
        return "user/agentWithdrawal"
    }

    // 탈퇴요청 일반회원 리스트
    @GetMapping("/withdrawal")
    fun getDeleteReqUserList(@ModelAttribute("searchForm") adminUserWithdrawalSearch: AdminUserWithdrawalSearch
                             , @ModelAttribute("withdrawalList") adminWithdrawalList: AdminWithdrawalList
                             , model: Model
                             , @PageableDefault(size=10, page=0) pageable: Pageable): String {
        setModel(NONE, model, pageable, UserSearchFilter.values().toList())
        model.addAttribute("userList", userRepository.getUserWithdrawalReqResult(adminUserWithdrawalSearch, pageable))
        return "user/userWithdrawal"
    }

    @PostMapping("/withdrawal/{type}")
    fun withdrawalAgent(@PathVariable("type") type: String,@ModelAttribute("withdrawalList") adminWithdrawalList: AdminWithdrawalList): String{
        adminUserService.withdrawalUser(adminWithdrawalList)
        if (type == "agent") {
            return "redirect:/admin/user/withdrawal/agent"
        }
        return "redirect:/admin/user/withdrawal"
    }

    @GetMapping("/withdrawal/reason/{id}")
    fun withdrawalDetail(@PathVariable id: Long, model: Model): String {
        val findUser = userRepository.findById(id).get()
        model.addAttribute("user", findUser)

        return "user/withdrawalDetail"
    }
}