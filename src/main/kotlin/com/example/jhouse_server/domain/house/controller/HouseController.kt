package com.example.jhouse_server.domain.house.controller

import com.example.jhouse_server.domain.house.dto.*
import com.example.jhouse_server.domain.house.service.HouseService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.aop.EnableValidation
import com.example.jhouse_server.global.response.ApplicationResponse
import org.aspectj.weaver.ast.Not
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@EnableValidation
@RestController
@RequestMapping("/api/v1/houses")
class HouseController(
    val houseService: HouseService
) {

    /**
     * 빈집 게시글 작성 ( 일반 사용자의 경우, 신청 상태로 변경 )
     *
     * @author dldmsql
     * @param req HouseReqDto 빈집 게시글 작성 시, 입력되는 데이터
     * @param user User RefreshToken 기반 인증/인가 처리된 사용자 데이터
     * @return house_id 빈집 게시글 ID
     * @exception INVALID_VALUE_EXCEPTION C0002 올바르지 않은 요청입니다. ( 유효성 검사 실패 시 )
     * */
    @Auth
    @PostMapping
    fun createHouse(
        @RequestBody @Validated req: HouseReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(houseService.createHouse(req, user))
    }

    /**
     * 빈집 게시글 수정
     *
     * @author dldmsql
     * @param houseId Long 빈집 게시글 ID
     * @param req HouseReqDto 빈집 게시글 작성 시, 입력되는 데이터
     * @param user User RefreshToken 기반 인증/인가 처리된 사용자 데이터
     * @return house_id 빈집 게시글 ID
     * @exception INVALID_VALUE_EXCEPTION C0002 올바르지 않은 요청입니다. ( 유효성 검사 실패 시 )
     * */
    @Auth
    @PutMapping("/{houseId}")
    fun updateHouse(
        @PathVariable houseId: Long,
        @RequestBody @Validated req: HouseReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Long> {
        return ApplicationResponse.ok(houseService.updateHouse(houseId, req, user))
    }

    /**
     * 빈집 게시글 삭제
     *
     * @author dldmsql
     * @param houseId Long 빈집 게시글 ID
     * @param user User
     * */
    @Auth
    @DeleteMapping("/{houseId}")
    fun deleteHouse(
        @PathVariable houseId: Long,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        houseService.deleteHouse(houseId, user)
        return ApplicationResponse.ok()
    }

    /**
     * 빈집 게시글 리스트 조회
     *
     * @author dldmsql
     * @param houseListDto HouseListDto 게시글 조회를 위한 기본 검색 조건 ( 매물 타입, 도시, 검색어 )
     * @param pageable Pageable 페이징처리를 위한 인터페이스
     * @return Page<HouseResDto>
     * */
    @GetMapping
    fun getHouseAll(
        @ModelAttribute houseListDto: HouseListDto,
        @PageableDefault(size=8, page=0) pageable: Pageable
    ) : ApplicationResponse<Page<HouseResDto>> {
        return ApplicationResponse.ok(houseService.getHouseAll(houseListDto, pageable))
    }

    /**
     * 빈집 게시글 상세 조회
     *
     * @author dldmsql
     * @param houseId Long 빈집 게시글 ID
     * @return HouseResOneDto
     * */
    @GetMapping("/{houseId}")
    fun getHouseOne(
        @PathVariable houseId: Long
    ) : ApplicationResponse<HouseResOneDto> {
        return ApplicationResponse.ok(houseService.getHouseOne(houseId))
    }

    /**
     * 빈집 게시글 신고
     *
     * @author dldmsql
     * @param houseId Long 빈집 게시글 ID
     * @param user User 현재 로그인한 유저
     * @return nothing
     * */
    @Auth
    @PutMapping("/report/{houseId}")
    fun reportHouse(
        @PathVariable houseId: Long,
        @RequestBody reportReqDto: ReportReqDto,
        @AuthUser user: User
    ) : ApplicationResponse<Nothing> {
        houseService.reportHouse(houseId, reportReqDto, user)
        return ApplicationResponse.ok()
    }
}