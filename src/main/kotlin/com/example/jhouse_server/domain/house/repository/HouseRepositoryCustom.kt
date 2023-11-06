package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.admin.house.dto.AdminHouseDto
import com.example.jhouse_server.admin.house.dto.AdminHouseSearch
import com.example.jhouse_server.domain.house.dto.HouseAgentListDto
import com.example.jhouse_server.domain.house.dto.HouseListDto
import com.example.jhouse_server.domain.house.dto.MyHouseResDto
import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface HouseRepositoryCustom {
    fun getHouseAll(houseListDto: HouseListDto, pageable: Pageable) : Page<House>

    fun getTmpSaveHouseAll(user: User, pageable: Pageable) : Page<House>

    fun getScrapHouseAll(user: User, filter: String?, pageable: Pageable): Page<House>

    fun getAgentHouseAll(user: User, houseAgentListDto: HouseAgentListDto, pageable: Pageable): Page<MyHouseResDto>

    fun getApplyHouseListWithPaging(adminHouseSearch: AdminHouseSearch, pageable: Pageable) : Page<AdminHouseDto>

    fun getMyHouseAll(user: User, keyword: String?, pageable: Pageable) : Page<MyHouseResDto>
}