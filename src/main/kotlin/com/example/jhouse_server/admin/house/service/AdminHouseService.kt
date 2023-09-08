package com.example.jhouse_server.admin.house.service

import com.example.jhouse_server.admin.house.dto.AdminHouseApplyList
import com.example.jhouse_server.admin.house.dto.AdminHouseDto
import com.example.jhouse_server.admin.house.dto.AdminHouseSearch
import com.example.jhouse_server.admin.house.dto.RejectForm
import com.example.jhouse_server.domain.house.dto.HouseResOneDto
import com.example.jhouse_server.domain.house.dto.toDto
import com.example.jhouse_server.domain.house.entity.HouseReviewStatus
import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Service
@Transactional(readOnly = true)
class AdminHouseService(
    val houseRepository: HouseRepository
) {
    /**
     * 일반 사용자가 작성한 빈집 게시글은 무조건 APPLY 상태
     * */
    fun getSearchApplyHouseResult(
        adminHouseSearch: AdminHouseSearch,
        pageable: Pageable
    ): Page<AdminHouseDto> {
        return houseRepository.getApplyHouseListWithPaging(adminHouseSearch, pageable)
    }

    fun getHouseDetail(id: Long): HouseResOneDto {
        return houseRepository.findByIdOrThrow(id).run { toDto(this, false) }
    }

    @Transactional
    fun updateReviewStatusApprove(
        reviewStatus: HouseReviewStatus,
        applyList: AdminHouseApplyList,
        redirectAttributes: RedirectAttributes
    ) {
        val findHouseList = applyList.applyHouseList?.let { houseRepository.findAllById(it) }
        // 이미 승인된 게시글이 포함되어 있다면 alert로 알리기
        findHouseList?.forEach{
            h -> if (h.applied == HouseReviewStatus.APPROVE) redirectAttributes.addAttribute("already_approve", "이미 승인된 거래 게시글이 포함되어 있습니다.")
        }
        if(redirectAttributes.containsAttribute("already_approve")) return
        findHouseList?.forEach{ h -> h.approveEntity()}
    }

    @Transactional
    fun updateReviewStatusReject(
        reviewStatus: HouseReviewStatus,
        rejectForm : RejectForm,
        redirectAttributes: RedirectAttributes
    ) {
        val findHouse = houseRepository.findByIdOrThrow(rejectForm.houseId)
        // 이미 승인된 게시글이 포함되어 있다면 alert로 알리기
        if (findHouse.applied == HouseReviewStatus.APPROVE) redirectAttributes.addAttribute("already_approve", "이미 승인된 거래 게시글이 포함되어 있습니다.")
        if(redirectAttributes.containsAttribute("already_approve")) return

        rejectForm.reason?.let { findHouse.rejectEntity(it) }
    }
}
