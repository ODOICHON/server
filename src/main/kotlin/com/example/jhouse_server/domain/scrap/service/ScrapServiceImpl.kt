package com.example.jhouse_server.domain.scrap.service

import com.example.jhouse_server.domain.house.repository.HouseRepository
import com.example.jhouse_server.domain.scrap.entity.Scrap
import com.example.jhouse_server.domain.scrap.repository.ScrapRepository
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.util.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class ScrapServiceImpl(
    val scrapRepository: ScrapRepository,
    val houseRepository: HouseRepository
): ScrapService {

    @Transactional
    override fun scrapHouse(houseId: Long, user: User): Long {
        val house = houseRepository.findByIdOrThrow(houseId)
        if(scrapRepository.existsByHouseAndSubscriber(house, user)) {
            throw ApplicationException(ErrorCode.ALREADY_SCRAP)
        }
        val scrap = Scrap(
            user, house
        )
        scrapRepository.save(scrap)
        return house.addScrap(scrap).id
    }

    @Transactional
    override fun unScrapHouse(houseId: Long, user: User) {
        val house = houseRepository.findByIdOrThrow(houseId)
        val scrap = scrapRepository.findByHouseAndSubscriber(house, user)
        house.deleteScrap(scrap)
        scrapRepository.deleteById(scrap.id)
    }
}