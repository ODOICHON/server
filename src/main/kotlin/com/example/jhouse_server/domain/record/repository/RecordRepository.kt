package com.example.jhouse_server.domain.record.repository

import com.example.jhouse_server.domain.record.entity.Record
import org.springframework.data.jpa.repository.JpaRepository

interface RecordRepository: JpaRepository<Record, Long> {
}