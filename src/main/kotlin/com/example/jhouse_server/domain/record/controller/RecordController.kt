package com.example.jhouse_server.domain.record.controller

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.user.entity.Authority.ADMIN
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/record")
class RecordController(
    private val recordService: RecordService
) {

    @Auth(ADMIN)
    @PostMapping
    fun saveRecord(
        @RequestBody recordReqDto: RecordReqDto,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordService.saveRecord(recordReqDto, user))
    }

    @Auth(ADMIN)
    @PutMapping("/{record_id}")
    fun updateRecord(
        @RequestBody recordUpdateDto: RecordUpdateDto,
        @AuthUser user: User,
        @PathVariable("record_id") recordId: Long
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordService.updateRecord(recordUpdateDto, user, recordId))
    }

    @Auth(ADMIN)
    @DeleteMapping("/{record_id}")
    fun deleteRecord(
        @AuthUser user: User,
        @PathVariable("record_id") recordId: Long
    ): ApplicationResponse<Nothing> {
        recordService.deleteRecord(user, recordId)
        return ApplicationResponse.ok()
    }

    @GetMapping("/hot")
    fun getHotRecords(): ApplicationResponse<RecordHotResDto> {
        return ApplicationResponse.ok(recordService.getHotRecords())
    }

    @GetMapping("/{part}/{dType}")
    fun getRecords(
        @PathVariable("part") part: String,
        @PathVariable("dType") dType: String,
        @RequestParam(value = "category", required = false) category: String,
        @PageableDefault(size = 4) pageable: Pageable
    ): ApplicationResponse<RecordPageResDto> {
        return ApplicationResponse.ok(recordService.getRecords(RecordPageCondition(part, dType, category), pageable))
    }

    @GetMapping("/{recordId}")
    fun getRecord(
        @PathVariable("recordId") recordId: Long,
        request: HttpServletRequest
    ): ApplicationResponse<RecordResDto> {
        return ApplicationResponse.ok(recordService.getRecord(recordId, request))
    }

    @Auth(ADMIN)
    @GetMapping("/review/{recordId}")
    fun getRecordWithReview(
        @PathVariable("recordId") recordId: Long
    ): ApplicationResponse<RecordWithReviewResDto> {
        return ApplicationResponse.ok(recordService.getRecordWithReview(recordId))
    }

    @Auth(ADMIN)
    @GetMapping("/reviewee")
    fun getRevieweeRecords(
        @RequestParam("status", required = false) status: String?,
        @AuthUser user: User,
        @PageableDefault(size = 3) pageable: Pageable
    ): ApplicationResponse<RecordPageResDto> {
        return ApplicationResponse.ok(recordService.getRevieweeRecords(RecordReviewCondition(status), user, pageable))
    }

    @Auth(ADMIN)
    @GetMapping("/reviewer")
    fun getReviewerRecords(
        @RequestParam("status", required = false) status: String?,
        @AuthUser user: User,
        @PageableDefault(size = 3) pageable: Pageable
    ): ApplicationResponse<RecordPageResDto> {
        return ApplicationResponse.ok(recordService.getReviewerRecords(RecordReviewCondition(status), user, pageable))
    }
}