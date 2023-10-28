package com.example.jhouse_server.domain.record.controller

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.record.service.RecordService
import com.example.jhouse_server.domain.user.entity.Authority.ADMIN
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.bucket.RateLimitService
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/v1/record")
class RecordController(
    /**
     * =============================================================================================
     * DI for Service
     * =============================================================================================
     */
    private val recordService: RecordService,
    private val rateLimitService: RateLimitService
) {

    /**
     * =============================================================================================
     * 블로그 게시글 작성
     *
     * @param recordReqDto
     * @param user
     * =============================================================================================
     */
    @Auth(ADMIN)
    @PostMapping
    fun saveRecord(
        @Validated @RequestBody recordReqDto: RecordReqDto,
        @AuthUser user: User
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordService.saveRecord(recordReqDto, user))
    }
    /**
     * =============================================================================================
     * 블로그 게시글 수정
     *
     * @param recordId
     * @param recordUpdateDto
     * @param user
     * =============================================================================================
     */
    @Auth(ADMIN)
    @PutMapping("/{record_id}")
    fun updateRecord(
        @Validated @RequestBody recordUpdateDto: RecordUpdateDto,
        @AuthUser user: User,
        @PathVariable("record_id") recordId: Long
    ): ApplicationResponse<Long> {
        return ApplicationResponse.ok(recordService.updateRecord(recordUpdateDto, user, recordId))
    }
    /**
     * =============================================================================================
     * 블로그 게시글 삭제
     *
     * @param recordId
     * @param user
     * =============================================================================================
     */
    @Auth(ADMIN)
    @DeleteMapping("/{record_id}")
    fun deleteRecord(
        @AuthUser user: User,
        @PathVariable("record_id") recordId: Long
    ): ApplicationResponse<Nothing> {
        recordService.deleteRecord(user, recordId)
        return ApplicationResponse.ok()
    }
    /**
     * =============================================================================================
     * 금주의 핫 게시글 조회
     * =============================================================================================
     */
    @GetMapping("/hot")
    fun getHotRecords(): ApplicationResponse<RecordHotResDto> {
        return ApplicationResponse.ok(recordService.getHotRecords())
    }
    /**
     * =============================================================================================
     * 블로그 목록 조회
     *
     * @param pageable
     * @param part
     * @param type
     * @param category
     * =============================================================================================
     */
    @GetMapping("/{part}/{type}")
    fun getRecords(
        @PathVariable("part") part: String,
        @PathVariable("type") type: String,
        @RequestParam(value = "category", defaultValue = "") category: String,
        @PageableDefault(size = 4) pageable: Pageable
    ): ApplicationResponse<RecordPageResDto> {
        return ApplicationResponse.ok(recordService.getRecords(RecordPageCondition(part, type, category), pageable))
    }
    /**
     * =============================================================================================
     * 블로그 게시글 상세 조회
     *
     * @param recordId
     * @param request
     * @param pageable
     * =============================================================================================
     */
    @GetMapping("/{record_id}")
    fun getRecord(
        @PathVariable("record_id") recordId: Long,
        request: HttpServletRequest,
        @PageableDefault(size = 10) pageable: Pageable
    ): ApplicationResponse<RecordResDto> {
        return ApplicationResponse.ok(recordService.getRecord(recordId, rateLimitService.getClientIp(request), pageable))
    }
    /**
     * =============================================================================================
     * 블로그 게시글 리뷰 조회
     *
     * @param recordId
     * =============================================================================================
     */
    @Auth(ADMIN)
    @GetMapping("/review/{record_id}")
    fun getRecordWithReview(
        @PathVariable("record_id") recordId: Long
    ): ApplicationResponse<RecordWithReviewResDto> {
        return ApplicationResponse.ok(recordService.getRecordWithReview(recordId))
    }
    /**
     * =============================================================================================
     * 리뷰 대기 게시글 조회
     *
     * @param status
     * @param user
     * @param pageable
     * =============================================================================================
     */
    @Auth(ADMIN)
    @GetMapping("/reviewee")
    fun getRevieweeRecords(
        @RequestParam("status", required = false) status: String?,
        @AuthUser user: User,
        @PageableDefault(size = 3) pageable: Pageable
    ): ApplicationResponse<RecordPageResDto> {
        return ApplicationResponse.ok(recordService.getRevieweeRecords(RecordReviewCondition(status), user, pageable))
    }
    /**
     * =============================================================================================
     * 내가 리뷰한 게시글 조회
     *
     * @param status
     * @param user
     * @param pageable
     * =============================================================================================
     */
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