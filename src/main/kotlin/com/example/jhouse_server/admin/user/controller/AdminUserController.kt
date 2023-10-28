package com.example.jhouse_server.admin.user.controller

import com.example.jhouse_server.admin.user.dto.*
import com.example.jhouse_server.admin.user.service.AdminUserService
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.UserType.AGENT
import com.example.jhouse_server.domain.user.entity.UserType.NONE
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.WAIT
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.repository.AgentRepository
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/admin/user")
class AdminUserController(
    /**
     * =============================================================================================
     *  DI for Service
     * =============================================================================================
     * */
    val userRepository: UserRepository,
    val agentRepository: AgentRepository,
    val adminUserService: AdminUserService
) {

    /**
     * =============================================================================================
     *  공인중개사 회원 가입 요청 목록 조회
     *  @param adminAgentSearch
     *  @param adminJoinAgentList
     *  @param model
     *  @param pageable
     *  @return user/agentJoin
     * =============================================================================================
     * */
    @GetMapping("/join")
    fun getAgentList(@ModelAttribute("searchForm") adminAgentSearch: AdminAgentSearch,
                     @ModelAttribute("joinList") adminJoinAgentList: AdminJoinAgentList,
                     model: Model,
                     @PageableDefault(size=10, page=0) pageable: Pageable): String{
        // 페이징 처리
        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        val total = agentRepository.countByStatus(AgentStatus.WAIT)
        model.addAttribute("total", total)

        // 데이터
        model.addAttribute("agentList", userRepository.getWaitingAgentResult(adminAgentSearch, pageable))
        model.addAttribute("filterList", AgentSearchFilter.values())
        return "user/agentJoin"

    }
    /**
     * =============================================================================================
     *  공인중개사 회원 가입 승인
     *  @param adminJoinAgentList
     *  @return redirect:/admin/user/join
     * =============================================================================================
     * */
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
    /**
     * =============================================================================================
     *  탈퇴한 공인중개사 목록 조회
     *  @param adminAgentSearch
     *  @param adminWithdrawalList
     *  @param model
     *  @param pageable
     *  @return user/agentWithdrawal
     * =============================================================================================
     * */
    @GetMapping("/withdrawal/agent")
    fun getDeleteReqAgentList(@ModelAttribute("searchForm") adminAgentSearch: AdminAgentSearch
                              , @ModelAttribute("withdrawalList") adminWithdrawalList: AdminWithdrawalList
                              , model: Model
                              , @PageableDefault(size=10, page=0) pageable: Pageable): String{
        setModel(AGENT, model, pageable, AgentSearchFilter.values().toList())
        model.addAttribute("agentList", userRepository.getAgentWithdrawalReqResult(adminAgentSearch, pageable))
        return "user/agentWithdrawal"
    }
    /**
     * =============================================================================================
     *  탈퇴한 일반 사용자 목록 조회
     *  @param adminUserWithdrawalSearch
     *  @param adminWithdrawalList
     *  @param model
     *  @param pageable
     *  @return user/userWithdrawal
     * =============================================================================================
     * */
    @GetMapping("/withdrawal")
    fun getDeleteReqUserList(@ModelAttribute("searchForm") adminUserWithdrawalSearch: AdminUserWithdrawalSearch
                             , @ModelAttribute("withdrawalList") adminWithdrawalList: AdminWithdrawalList
                             , model: Model
                             , @PageableDefault(size=10, page=0) pageable: Pageable): String {
        setModel(NONE, model, pageable, UserSearchFilter.values().toList())
        model.addAttribute("userList", userRepository.getUserWithdrawalReqResult(adminUserWithdrawalSearch, pageable))
        return "user/userWithdrawal"
    }
    /**
     * =============================================================================================
     *  회원 탈퇴 처리
     *  @param type
     *  @param adminWithdrawalList
     *  @return redirect:/admin/user/withdrawal || redirect:/admin/user/withdrawal/agent
     * =============================================================================================
     * */
    @PostMapping("/withdrawal/{type}")
    fun withdrawalAgent(
        @PathVariable("type") type: String,
        @ModelAttribute("withdrawalList") adminWithdrawalList: AdminWithdrawalList): String{
        adminUserService.withdrawalUser(adminWithdrawalList)
        if (type == "agent") {
            return "redirect:/admin/user/withdrawal/agent"
        }
        return "redirect:/admin/user/withdrawal"
    }
    /**
     * =============================================================================================
     *  회원 탈퇴 사용자 정보 상세 조회
     *  @param id
     *  @param model
     *  @return user/withdrawalDetail
     * =============================================================================================
     * */
    @GetMapping("/withdrawal/reason/{id}")
    fun withdrawalDetail(
        @PathVariable id: Long,
        model: Model): String {
        val findUser = userRepository.findById(id).get()
        model.addAttribute("user", findUser)

        return "user/withdrawalDetail"
    }
}