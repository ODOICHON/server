package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.house.dto.*
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HouseService {
    fun createHouse(req: HouseReqDto, user: User): Long
    fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<HouseResDto>
    fun updateHouse(houseId: Long, req: HouseReqDto, user: User): Long
    fun deleteHouse(houseId: Long, user: User)
    fun getHouseOne(houseId: Long): HouseResOneDto
    fun reportHouse(houseId: Long, reportReqDto: ReportReqDto, user: User)
    fun getHouseOneWithUser(houseId: Long, user: User): HouseResOneDto
    fun getTmpSaveHouseAll(user: User, pageable: Pageable): Page<HouseResDto>
    fun updateStatus(user: User, houseId: Long, dealReqDto: DealReqDto)
    fun getScrapHouseAll(user: User, pageable: Pageable): Page<HouseResDto>
    fun getAgentHouseAll(user: User, houseAgentListDto: HouseAgentListDto, pageable: Pageable): Page<MyHouseResDto>
    abstract fun getMyHouseAll(user: User, keyword: String?, filter: String?, pageable: Pageable): Page<MyHouseResDto>

}
