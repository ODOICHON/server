package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record.repository.odori.OdoriRepositoryCustom
import com.example.jhouse_server.domain.record.repository.retrospection.RetrospectionRepositoryCustom
import com.example.jhouse_server.domain.record.repository.technology.TechnologyRepositoryCustom
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.*

interface RecordRepository: JpaRepository<Record, Long>, RecordRepositoryCustom {

    @Query("select dtype from record where id = :recordId", nativeQuery = true)
    fun findDType(@Param("recordId") recordId: Long): String

    @Query("select r from Record r where r.createdAt >= :weekAgo and r.status = 'WAIT'")
    fun findWeekAgo(@Param("weekAgo") weekAgo: LocalDateTime): List<Record>

    @EntityGraph(attributePaths = ["user"])
    @Query("select r from Record r where r.id = :recordId")
    fun findByIdWithUser(@Param("recordId") recordId: Long): Optional<Record>
}