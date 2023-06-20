package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.board.PrefixCategory
import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.board.entity.BoardCategory
import com.example.jhouse_server.domain.board.entity.BoardCode
import com.example.jhouse_server.domain.board.repository.BoardCodeRepository
import com.example.jhouse_server.domain.board.repository.BoardRepository
import com.example.jhouse_server.domain.board.service.getContent
import com.example.jhouse_server.domain.house.dto.*
import com.example.jhouse_server.domain.house.entity.Address
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class HouseServiceImpl(
    val houseRepository: HouseRepository,
    val boardRepository: BoardRepository,
) : HouseService {

    @Transactional
    override fun createHouse(req: HouseReqDto, user: User): Long {
        val address = Address(req.city!!, req.zipCode!!)
        val content = getContent(req.code!!)
        val house = House(req.rentalType!!, address, req.size!!, req.purpose!!, req.floorNum,
            req.sumFloor, req.contact!!, req.createdDate, req.price!!, req.monthlyPrice,
            req.agentName, req.title, content, req.code, req.imageUrls, user)
        return houseRepository.save(house).id
    }

    override fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<HouseResDto> {
        return houseRepository.getHouseAll(houseListDto, pageable).map{ toListDto(it) }
    }

    @Transactional
    override fun updateHouse(houseId: Long, req: HouseReqDto, user: User): Long {
        val house = houseRepository.findByIdOrThrow(houseId)
        house.address.updateEntity(req.city!!, req.zipCode!!)
        if (user != house.user) throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
        val content = getContent(req.code!!)
        return house.updateEntity(
            req.rentalType!!, req.size!!, req.purpose!!, req.floorNum, req.sumFloor, req.contact!!,
            req.createdDate, req.price!!, req.monthlyPrice, req.agentName, req.title, content, req.code, req.imageUrls
        ).id
    }

    @Transactional
    override fun deleteHouse(houseId: Long, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        if (user == house.user || user.authority == Authority.ADMIN) house.deleteEntity()
        else throw ApplicationException(ErrorCode.UNAUTHORIZED_EXCEPTION)
    }

    override fun getHouseOne(houseId: Long): HouseResOneDto {
        return houseRepository.findByIdOrThrow(houseId).run { toDto(this) }
    }
}