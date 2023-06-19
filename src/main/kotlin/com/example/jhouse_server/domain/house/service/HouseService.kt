package com.example.jhouse_server.domain.house.service

import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.dto.HouseReqDto
import com.example.jhouse_server.domain.house.dto.HouseResDto
import com.example.jhouse_server.domain.house.dto.HouseResOneDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HouseService {
    fun createHouse(req: HouseReqDto, user: User): Long
    fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable): Page<HouseResDto>
    fun updateHouse(houseId: Long, req: HouseReqDto, user: User): Long
    fun deleteHouse(houseId: Long, user: User)
    fun getHouseOne(houseId: Long): HouseResOneDto

}
