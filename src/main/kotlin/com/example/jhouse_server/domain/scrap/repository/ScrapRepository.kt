package com.example.jhouse_server.domain.scrap.repository

import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.scrap.entity.Scrap
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ScrapRepository : JpaRepository<Scrap, Long> {

    fun existsByHouseAndSubscriber(house: House, subscriber: User) : Boolean
    fun findByHouseAndSubscriber(house: House, subscriber: User) : Scrap
}