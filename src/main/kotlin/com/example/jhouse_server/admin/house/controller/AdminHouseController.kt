package com.example.jhouse_server.admin.house.controller

import com.example.jhouse_server.admin.house.dto.AdminHouseApplyList
import com.example.jhouse_server.admin.house.dto.AdminHouseSearch
import com.example.jhouse_server.admin.house.dto.HouseSearchFilter
import com.example.jhouse_server.admin.house.dto.RejectForm
import com.example.jhouse_server.admin.house.service.AdminHouseService
import com.example.jhouse_server.domain.house.entity.HouseReviewStatus
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/admin/house")
class AdminHouseController(
    /**
     * =============================================================================================
     *  DI for Service
     * =============================================================================================
     * */
    val adminHouseService: AdminHouseService
) {
    /**
     * =============================================================================================
     * 빈집 게시글 승인 페이지
     *
     * @author dldmsql
     * @param adminHouseSearch
     * @param adminHouseApplyList
     * @param model
     * @param pageable
     * @return house/houses
     * =============================================================================================
     * */
    @GetMapping("/apply")
    fun getHouses(
        @ModelAttribute("searchForm") adminHouseSearch: AdminHouseSearch,
        @ModelAttribute("applyList") adminHouseApplyList: AdminHouseApplyList,
        model: Model,
        @PageableDefault(size=10, page=0) pageable: Pageable
    ): String {

        // 승인 요청된 게시글 목록 데이터
        val result = adminHouseService.getSearchApplyHouseResult(adminHouseSearch, pageable)
        model.addAttribute("houseList", result)

        // 페이징 데이터
        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        model.addAttribute("filterList", HouseSearchFilter.values())

        return "house/houses"
    }
    /**
     * =============================================================================================
     * 빈집 게시글 상세 조회
     *
     * @author dldmsql
     * @param houseId
     * @param rejectForm
     * @param model
     * @return house/houseDetail
     * =============================================================================================
     * */
    @GetMapping("/{houseId}")
    fun getHouseDetail(
        @PathVariable("houseId") houseId: Long,
        @ModelAttribute("rejectForm") rejectForm: RejectForm,
        model: Model
    ) : String {
        // 빈집 게시글 상세 데이터
        val result = adminHouseService.getHouseDetail(houseId)
        model.addAttribute("houseDto", result)
        return "house/houseDetail"
    }
    /**
     * =============================================================================================
     * 빈집 게시글 승인 처리
     *
     * @author dldmsql
     * @param applyList
     * @param redirectAttributes
     * @return redirect:/admin/house/apply
     * =============================================================================================
     * */
    @PostMapping("/apply/approve")
    fun updateReviewStatusApprove(
        @ModelAttribute("applyList") applyList: AdminHouseApplyList,
        redirectAttributes: RedirectAttributes
    ) : String {
        adminHouseService.updateReviewStatusApprove(HouseReviewStatus.APPROVE, applyList, redirectAttributes)
        return "redirect:/admin/house/apply"
    }
    /**
     * =============================================================================================
     * 빈집 게시글 반려 처리
     *
     * @author dldmsql
     * @param rejectForm
     * @param redirectAttributes
     * @return redirect:/admin/house/apply
     * =============================================================================================
     * */
    @PostMapping("/apply/reject")
    fun updateReviewStatusReject(
        @ModelAttribute("rejectForm") rejectForm: RejectForm,
        redirectAttributes: RedirectAttributes
    ) : String {
        adminHouseService.updateReviewStatusReject(HouseReviewStatus.REJECT, rejectForm, redirectAttributes)
        return "redirect:/admin/house/apply"
    }
    /**
     * =============================================================================================
     * 빈집 거래 후기 관리
     *
     * @author dldmsql
     * @param adminHouseSearch
     * @param model
     * @param pageable
     * @return house/houseReview
     * =============================================================================================
     * */
    @GetMapping("/review")
    fun getHousesReview(
        @ModelAttribute("searchForm") adminHouseSearch: AdminHouseSearch,
        model: Model,
        @PageableDefault(size=10, page=0) pageable: Pageable
    ): String {
        // 승인 요청된 게시글 목록 데이터
        val result = adminHouseService.getSearchReviewHouseResult(adminHouseSearch, pageable)
        model.addAttribute("dealList", result)

        // 페이징 데이터
        val pageCom = pageable.pageNumber / 5
        model.addAttribute("pageCom", pageCom)
        model.addAttribute("filterList", HouseSearchFilter.values())

        return "house/houseReview"
    }
}