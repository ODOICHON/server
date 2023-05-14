package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record.repository.odori.OdoriRepositoryCustom
import com.example.jhouse_server.domain.record.repository.retrospection.RetrospectionRepositoryCustom
import com.example.jhouse_server.domain.record.repository.technology.TechnologyRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RecordRepository: JpaRepository<Record, Long>, RecordRepositoryCustom {

    @Query("select dtype from record where id = :recordId", nativeQuery = true)
    fun findDType(@Param("recordId") recordId: Long): String
}