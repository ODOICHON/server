package com.example.jhouse_server.admin.report.controller

import com.example.jhouse_server.admin.report.dto.AdminReportSearchFilter
import com.example.jhouse_server.admin.report.dto.ReportAgentList
import com.example.jhouse_server.admin.report.dto.ReportSearch
import com.example.jhouse_server.admin.report.service.AdminReportService
import com.example.jhouse_server.domain.house.repository.ReportRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@RequestMapping("/admin/report")
@Controller
class AdminReportController(
    /**
     * =============================================================================================
     *  DI for Service
     * =============================================================================================
     * */
    val reportRepository: ReportRepository,
    val adminReportService: AdminReportService
) {
    /**
     * =============================================================================================
     * 신고 유저 목록 조회
     * @param reportSearch
     * @param agentList
     * @param model
     * @param pageable
     * @return report/reports
     * =============================================================================================
     * */
    @GetMapping
    fun getReports(@ModelAttribute("searchForm") reportSearch: ReportSearch
                   , @ModelAttribute("agentList") agentList: ReportAgentList
                   , model: Model
                   , @PageableDefault(size=10, page=0) pageable: Pageable): String{

        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        model.addAttribute("filterList", AdminReportSearchFilter.values())
        model.addAttribute("reportList", reportRepository.getReports(reportSearch, pageable))

        return "report/reports"
    }
    /**
     * =============================================================================================
     * 신고 유저 상세 조회
     * @param id
     * @param model
     * @return report/reportDetail
     * =============================================================================================
     * */
    @GetMapping("{agent_id}")
    fun getReportDetail(
        @PathVariable("agent_id") id: Long,
        model: Model): String{
        val result = reportRepository.getReportDetail(id)
        model.addAttribute("reportList", result)
        return "report/reportDetail"
    }
    /**
     * =============================================================================================
     * 사용자 신고 상태 변경
     * @param type
     * @param agentList
     * @param redirectAttributes
     * @return redirect:/admin/report
     * =============================================================================================
     * */
    @PostMapping("{type}")
    fun changeUserSuspensionStatus(@PathVariable("type") type: String
                                   , @ModelAttribute("agentList") agentList: ReportAgentList
                                   , redirectAttributes: RedirectAttributes): String {
        if(type == "suspension") {
            adminReportService.suspensionUser(agentList, redirectAttributes)
        } else {
            adminReportService.cancelSuspensionUser(agentList, redirectAttributes)
        }
        return "redirect:/admin/report"
    }


}