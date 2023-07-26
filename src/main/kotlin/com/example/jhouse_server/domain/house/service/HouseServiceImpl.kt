package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.board.repository.dto.CustomPageImpl
import com.example.jhouse_server.domain.board.service.getContent
import com.example.jhouse_server.domain.house.dto.*
import com.example.jhouse_server.domain.house.entity.Address
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.Report
import com.example.jhouse_server.domain.house.entity.ReportType
import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.house.repository.ReportRepository
import com.example.jhouse_server.domain.scrap.repository.ScrapRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.exception.ErrorCode.*
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class HouseServiceImpl(
    val houseRepository: HouseRepository,
    val scrapRepository: ScrapRepository,
    val reportRepository: ReportRepository
) : HouseService {

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
        return houseRepository.save(house).id
    }
    @Cacheable(cacheNames = ["getCache"], cacheManager = "ehCacheCacheManager")
    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<HouseResDto> {
        val houseAll = houseRepository.getHouseAll(houseListDto, pageable).map { toListDto(it) }
        return CustomPageImpl(houseAll.content, houseAll.number, houseAll.size, houseAll.totalElements)
    }

    @Transactional
    override fun updateHouse(houseId: Long, req: HouseReqDto, user: User): Long {
        val house = houseRepository.findByIdOrThrow(houseId)
        house.address.updateEntity(req.city!!, req.zipCode!!)
        if (user != house.user) throw ApplicationException(UNAUTHORIZED_EXCEPTION)
        val content = getContent(req.code!!)
        if(!req.tmpYn) {
            house.saveEntity()
            if(user.userType == UserType.NONE) house.applyEntity()
        }
        return house.updateEntity(
            req.rentalType!!, req.size!!, req.purpose!!, req.floorNum, req.contact!!,
            req.createdDate, req.price!!, req.monthlyPrice, req.agentName, req.title, content, req.code, req.imageUrls
        ).id
    }

    @Transactional
    override fun deleteHouse(houseId: Long, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if (user == house.user || user.authority == Authority.ADMIN) house.deleteEntity()
        else throw ApplicationException(UNAUTHORIZED_EXCEPTION)
    }

    override fun getHouseOne(houseId: Long): HouseResOneDto {
        return houseRepository.findByIdOrThrow(houseId).run { toDto(this, false) }
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
}