package com.example.jhouse_server.domain.house.repository

import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.house.entity.Report
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReportRepository : JpaRepository<Report, Long>, ReportRepositoryCustom{

    fun existsByReporterAndHouse(reporter: User, house: House): Boolean

    fun findByOwner(owner: User): List<Report>

}