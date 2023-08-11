package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.HouseTag
import org.springframework.data.jpa.repository.JpaRepository

interface HouseTagRepository : JpaRepository<HouseTag, Long> {

    fun findAllByHouse(house: House) : MutableList<HouseTag>
}