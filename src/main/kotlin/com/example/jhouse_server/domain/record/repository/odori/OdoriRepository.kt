package com.example.jhouse_server.domain.record.repository.odori

import com.example.jhouse_server.domain.record.entity.odori.Odori
import org.springframework.data.jpa.repository.JpaRepository

interface OdoriRepository: JpaRepository<Odori, Long>, OdoriRepositoryCustom {
}