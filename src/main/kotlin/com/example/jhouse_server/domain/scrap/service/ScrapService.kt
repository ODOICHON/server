package com.example.jhouse_server.domain.scrap.service

import com.example.jhouse_server.domain.user.entity.User

interface ScrapService {
    fun scrapHouse(houseId: Long, user: User): Long
    fun unScrapHouse(houseId: Long, user: User)
}