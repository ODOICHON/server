package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.board.repository.dto.CustomPageImpl
import com.example.jhouse_server.domain.board.service.getContent
import com.example.jhouse_server.domain.house.dto.*
import com.example.jhouse_server.global.exception.ErrorCode


import com.example.jhouse_server.domain.house.entity.*
import com.example.jhouse_server.domain.house.repository.DealRepository
import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.house.repository.HouseTagRepository
import com.example.jhouse_server.domain.house.entity.Address
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.Report
import com.example.jhouse_server.domain.house.entity.ReportType
import com.example.jhouse_server.domain.house.repository.ReportRepository
import com.example.jhouse_server.domain.scrap.repository.ScrapRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode.*
import com.example.jhouse_server.global.exception.ReqValidationException
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class HouseServiceImpl(
    val houseRepository: HouseRepository,
    val scrapRepository: ScrapRepository,
    val userRepository: UserRepository,
    val dealRepository: DealRepository,
    val houseTagRepository: HouseTagRepository,
    val reportRepository: ReportRepository,
) : HouseService {
    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun createHouse(req: HouseReqDto, user: User): Long {
        var houseId = 0L
        // (1) 임시저장여부 확인
        if(req.tmpYn) {
            // (2) null 데이터 blank로 변경
            val tmpReq = changeNullToBlank(req)
            // (3) 임시저장 데이터 생성
            val address = Address(req.city!!, req.zipCode!!)
            val content = getContent(req.code!!)
            val tmp = House(tmpReq.rentalType!!, address, tmpReq.size!!, tmpReq.purpose!!, tmpReq.floorNum!!,
                tmpReq.contact!!, tmpReq.createdDate!!, tmpReq.price!!, tmpReq.monthlyPrice!!,
                tmpReq.agentName!!, tmpReq.title!!, content, tmpReq.code!!, tmpReq.imageUrls!!, user)
            // (4) 임시저장 상태로 변경
            tmp.tmpSaveEntity()
            // (5) 임시저장 게시글 저장
            val tmpSaved = houseRepository.save(tmp).id
            // (6) 추천태그 값이 있을 경우, 태그 저장
            if(!tmpReq.recommendedTag.isNullOrEmpty()) houseTagRepository.saveAll(createHouseTag(tmpReq.recommendedTag!!, tmp))
            houseId = tmpSaved
        } else {
            // (1) 유효성 검사
            if(validationReqDto(req)) {
                // (2) 게시글 생성
                val address = Address(req.city!!, req.zipCode!!)
                val content = getContent(req.code!!)
                val house = House(req.rentalType!!, address, req.size!!, req.purpose!!, req.floorNum!!,
                    req.contact!!, req.createdDate!!, req.price!!, req.monthlyPrice!!,
                    req.agentName!!, req.title!!, content, req.code!!, req.imageUrls!!, user)
                // (3) 게시글 상태 변경
                if(user.userType == UserType.AGENT || user.authority == Authority.ADMIN) house.approveEntity()
                else if (user.userType == UserType.NONE) house.applyEntity()
                // (4) 게시글 저장
                val saved = houseRepository.save(house).id
                // (5) 태그 저장
                if(!req.recommendedTag.isNullOrEmpty()) houseTagRepository.saveAll(createHouseTag(req.recommendedTag!!, house))
                houseId = saved
            }
        }
        return houseId
    }

    private fun changeNullToBlank(req: HouseReqDto): HouseReqDto {
        if(req.rentalType == null) req.rentalType = RentalType.SALE
        if(req.city.isNullOrBlank()) req.city = ""
        if(req.zipCode.isNullOrEmpty()) req.zipCode = ""
        if(req.size.isNullOrEmpty()) req.size = ""
        if(req.purpose.isNullOrEmpty()) req.purpose = ""
        if(req.contact.isNullOrEmpty()) req.contact = ""
        if(req.createdDate.isNullOrEmpty()) req.createdDate = ""
        if(req.code.isNullOrEmpty()) req.code = ""
        if(req.imageUrls.isNullOrEmpty()) req.imageUrls = mutableListOf("")
        if(req.title.isNullOrEmpty()) req.title = ""
        if(req.agentName.isNullOrEmpty()) req.agentName = ""
        return req
    }

        @Cacheable(cacheNames = ["getCache"], cacheManager = "ehCacheCacheManager", key = "#houseListDto.toString()+#pageable.pageNumber.toString()")
    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<HouseResDto> {
        val houseAll = houseRepository.getHouseAll(houseListDto, pageable).map { toListDto(it) }
        return CustomPageImpl(houseAll.content, houseAll.number, houseAll.size, houseAll.totalElements)
    }
    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun updateHouse(houseId: Long, req: HouseReqDto, user: User): Long {
        // (1) 기존 데이터 조회
        val house = houseRepository.findByIdOrThrow(houseId)
        // (2) 권한 체크 ( 작성자 본인이 아닌 경우 )
        if (user != house.user) throw ApplicationException(UNAUTHORIZED_EXCEPTION)
        house.address.updateEntity(req.city!!, req.zipCode!!)
        // (3) 연관 테이블 ( 주소 ) 수정
        val content = getContent(req.code!!)
        // (4) 임시저장 -> 등록 && 유효성 검사
        if(!req.tmpYn && validationReqDto(req)) {
            house.saveEntity()
            if(user.userType == UserType.NONE) house.applyEntity() // 게시글 저장 -> 관리자에게 승인요청
            if(user.userType == UserType.AGENT || user.authority == Authority.ADMIN) house.approveEntity()
        }
        // (5) 게시글 데이터 수정
        house.updateEntity(
            req.rentalType!!, req.size!!, req.purpose!!, req.floorNum!!, req.contact!!,
            req.createdDate!!, req.price!!, req.monthlyPrice!!, req.agentName!!, req.title!!, content, req.code!!, req.imageUrls!!
        )
        // (6) 게시글 태그 조회
        val originHouseTags = houseTagRepository.findAllByHouse(house)
        // (7) 게시글 태그 전체 삭제
        houseTagRepository.deleteAll(originHouseTags)
        // (8) 게시글 태그 저장
        if(!req.recommendedTag.isNullOrEmpty()) houseTagRepository.saveAll(createHouseTag(req.recommendedTag!!, house))
        return houseId
    }

    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun deleteHouse(houseId: Long, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if (user == house.user || user.authority == Authority.ADMIN) house.deleteEntity()
        else throw ApplicationException(UNAUTHORIZED_EXCEPTION)
    }

    override fun getHouseOne(houseId: Long): HouseResOneDto {
        val house = houseRepository.findByIdOrThrow(houseId)
        if(!house.useYn) throw ApplicationException(NOT_FOUND_EXCEPTION)
        return house.run { toDto(this, false) }
    }

    @Transactional
    override fun reportHouse(houseId: Long, reportReqDto: ReportReqDto, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if(house.user == user) throw ApplicationException(DONT_REPORT_HOUSE_MINE)
        else if(reportRepository.existsByReporterAndHouse(user, house)) throw ApplicationException(DUPLICATE_REPORT)
        val report = Report(house, user, ReportType.valueOf(reportReqDto.reportType), reportReqDto.reportReason)
        house.reportEntity()
        reportRepository.save(report)
    }

    override fun getHouseOneWithUser(houseId: Long, user: User): HouseResOneDto {
        val house = houseRepository.findByIdOrThrow(houseId)
        val isScraped = scrapRepository.existsByHouseAndSubscriber(house, user)
        return toDto(house, isScraped)
    }

    override fun getTmpSaveHouseAll(user: User, pageable: Pageable): Page<HouseResDto> {
        return houseRepository.getTmpSaveHouseAll(user, pageable).map { toListDto(it) }
    }

    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun updateStatus(user: User, houseId: Long, dealReqDto: DealReqDto) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if(user !== house.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        house.updateDealStatus()
        val buyer = if (!dealReqDto.nickName.isNullOrBlank()) userRepository.findByNickName(dealReqDto.nickName).get() else throw ApplicationException(INVALID_VALUE_EXCEPTION)
        val deal = Deal(LocalDate.parse(dealReqDto.dealDate), dealReqDto.score, dealReqDto.review, buyer, house)
        dealRepository.save(deal)
    }

    override fun getScrapHouseAll(user: User, pageable: Pageable): Page<HouseResDto> {
        return houseRepository.getScrapHouseAll(user, pageable).map { toListDto(it) }
    }

    override fun getAgentHouseAll(
        user: User,
        houseAgentListDto: HouseAgentListDto,
        pageable: Pageable
    ): Page<MyHouseResDto> {
        val housePage = houseRepository.getAgentHouseAll(user, houseAgentListDto, pageable)
            .map { toMyHouseDto(it) }
        return CustomPageImpl(housePage.content, housePage.number, housePage.size, housePage.totalElements)
    }

    override fun getMyHouseAll(
        user: User,
        keyword: String?,
        pageable: Pageable
    ): Page<MyHouseResDto> {
        val housePage = houseRepository.getMyHouseAll(user, keyword, pageable).map { toMyHouseDto(it) }
        return CustomPageImpl(housePage.content, housePage.number, housePage.size, housePage.totalElements)
    }

    private fun createHouseTag(recommendedTag : List<String> , house: House) : List<HouseTag> {
        val recommendedTags = recommendedTag.map { RecommendedTag.getTagByName(it) }.toList()
        val houseTags: MutableList<HouseTag> = mutableListOf()
        for (tag in recommendedTags) {
            houseTags.add(HouseTag(tag, house))
        }
        return houseTags
    }
    private fun validationReqDto(req: HouseReqDto) : Boolean {
        if(req.rentalType == null) throw ReqValidationException("매매 타입은 필수값입니다. ")
        if(req.city.isNullOrBlank()) throw ReqValidationException("주소는 필수값입니다.")
        if(req.zipCode.isNullOrEmpty()) throw ReqValidationException("우편변호는 필수값입니다.")
        if(req.size.isNullOrEmpty()) throw ReqValidationException("건물 크기는 필수값입니다.")
        if(req.purpose.isNullOrEmpty()) throw ReqValidationException("목적/용도는 필수값입니다.")
        if(req.contact.isNullOrEmpty()) throw ReqValidationException("연락 가능한 휴대폰번호는 필수값입니다.")
        if(req.createdDate.isNullOrEmpty()) throw ReqValidationException("준공연도는 필수값입니다.")
        if(req.code.isNullOrEmpty()) throw ReqValidationException("게시글 내용은 필수값입니다.")
        else if(getContent(req.code!!).length > 10000) throw ApplicationException(LENGTH_OUT_OF_CONTENTS)
        if(req.imageUrls.isNullOrEmpty() || req.imageUrls!!.size < 5) throw ReqValidationException("첨부 이미지는 5장 이상이어야 합니다.")
        return true
    }
}