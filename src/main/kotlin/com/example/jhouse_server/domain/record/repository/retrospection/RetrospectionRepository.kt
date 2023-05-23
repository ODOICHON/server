package com.example.jhouse_server.domain.record.repository.retrospection

import com.example.jhouse_server.domain.record.entity.retrospection.Retrospection
import org.springframework.data.jpa.repository.JpaRepository

interface RetrospectionRepository: JpaRepository<Retrospection, Long>, RetrospectionRepositoryCustom {
}