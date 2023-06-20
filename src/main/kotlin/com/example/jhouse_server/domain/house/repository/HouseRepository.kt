package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.entity.House
import org.springframework.data.jpa.repository.JpaRepository

interface HouseRepository : JpaRepository<House, Long>, HouseRepositoryCustom {
}