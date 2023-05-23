package com.example.jhouse_server.domain.record.repository.technology

import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordThumbnailResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TechnologyRepositoryCustom {

    fun findTechnologies(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto>
}