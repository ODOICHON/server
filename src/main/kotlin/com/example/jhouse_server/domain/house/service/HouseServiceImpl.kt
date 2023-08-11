package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.board.repository.dto.CustomPageImpl
import com.example.jhouse_server.domain.board.service.getContent
import com.example.jhouse_server.domain.house.dto.*
import com.example.jhouse_server.domain.house.entity.*
import com.example.jhouse_server.domain.house.repository.DealRepository
import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.house.repository.HouseTagRepository
import com.example.jhouse_server.domain.scrap.repository.ScrapRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils.hasText
import java.sql.Date
import kotlin.jvm.optionals.getOrElse

@Service
@Transactional(readOnly = true)
class HouseServiceImpl(
    val houseRepository: HouseRepository,
    val scrapRepository: ScrapRepository,
    val userRepository: UserRepository,
    val dealRepository: DealRepository,
    val houseTagRepository: HouseTagRepository,
) : HouseService {
    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun createHouse(req: HouseReqDto, user: User): Long {
        val address = Address(req.city!!, req.zipCode!!)
        val content = getContent(req.code!!)
        val house = House(req.rentalType!!, address, req.size!!, req.purpose!!, req.floorNum,
            req.contact!!, req.createdDate, req.price!!, req.monthlyPrice,
            req.agentName, req.title, content, req.code, req.imageUrls, user)
        if(req.tmpYn) house.tmpSaveEntity()
        if(user.userType == UserType.AGENT || user.authority == Authority.ADMIN) house.approveEntity()
        else if (!req.tmpYn && user.userType == UserType.NONE) house.applyEntity()
        val saved = houseRepository.save(house).id
        houseTagRepository.saveAll(createHouseTag(req.recommendedTag, house))
        return saved
    }

//    @Cacheable(cacheNames = ["getCache"], cacheManager = "ehCacheCacheManager", key = "#houseListDto.toString()+#pageable.pageNumber.toString()")
    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<HouseResDto> {
        val houseAll = houseRepository.getHouseAll(houseListDto, pageable).map { toListDto(it) }
        return CustomPageImpl(houseAll.content, houseAll.number, houseAll.size, houseAll.totalElements)
    }
    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun updateHouse(houseId: Long, req: HouseReqDto, user: User): Long {
        val house = houseRepository.findByIdOrThrow(houseId)
        house.address.updateEntity(req.city!!, req.zipCode!!)
        if (user != house.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        val content = getContent(req.code!!)
        if(!req.tmpYn) {
            house.saveEntity()
            if(user.userType == UserType.NONE) house.applyEntity() // 게시글 저장 -> 관리자에게 승인요청
        }
        house.updateEntity(
            req.rentalType!!, req.size!!, req.purpose!!, req.floorNum, req.contact!!,
            req.createdDate, req.price!!, req.monthlyPrice, req.agentName, req.title, content, req.code, req.imageUrls
        )
        val originHouseTags = houseTagRepository.findAllByHouse(house)
        houseTagRepository.deleteAll(originHouseTags)
        houseTagRepository.saveAll(createHouseTag(req.recommendedTag, house))
        return houseId
    }

    @CacheEvict(allEntries = true, cacheManager = "ehCacheCacheManager", value = ["getCache"])
    @Transactional
    override fun deleteHouse(houseId: Long, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if (user == house.user || user.authority == Authority.ADMIN) house.deleteEntity()
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    override fun getHouseOne(houseId: Long): HouseResOneDto {
        return houseRepository.findByIdOrThrow(houseId).run { toDto(this, false) }
    }

    @Transactional
    override fun reportHouse(houseId: Long, reportReqDto: ReportReqDto, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if(house.user == user) throw ApplicationException(ErrorCode.DONT_REPORT_HOUSE_MINE)
        else house.reportEntity(reportReqDto.reportReason, reportReqDto.reportType)
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
        val buyerId = if (dealReqDto.nickName != null && dealReqDto.nickName != "") userRepository.findByNickName(dealReqDto.nickName).get().id else null
        val deal = Deal(Date.valueOf(dealReqDto.dealDate), dealReqDto.score, dealReqDto.review, buyerId, house)
        dealRepository.save(deal)
    }


    private fun createHouseTag(recommendedTag : List<String> , house: House) : List<HouseTag> {
        val recommendedTags = recommendedTag.map { RecommendedTag.getTagByName(it) }.toList()
        val houseTags : MutableList<HouseTag> = mutableListOf()
        for(tag in recommendedTags) {
            houseTags.add(HouseTag(tag, house))
        }
        return houseTags
    }
}