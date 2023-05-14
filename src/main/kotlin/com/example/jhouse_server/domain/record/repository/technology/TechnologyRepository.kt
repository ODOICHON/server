package com.example.jhouse_server.domain.record.repository.technology

import com.example.jhouse_server.domain.record.entity.technology.Technology
import org.springframework.data.jpa.repository.JpaRepository

interface TechnologyRepository: JpaRepository<Technology, Long>, TechnologyRepositoryCustom {
}