package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.entity.House
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HouseRepositoryCustom {
    fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable) : Page<House>
}