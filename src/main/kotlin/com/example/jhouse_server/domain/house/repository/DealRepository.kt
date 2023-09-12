package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.entity.Deal
import com.example.jhouse_server.domain.house.entity.House
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface DealRepository : JpaRepository<Deal, Long>, DealRepositoryCustom{

    fun findByHouseId(houseId: Long) : Optional<Deal>
}