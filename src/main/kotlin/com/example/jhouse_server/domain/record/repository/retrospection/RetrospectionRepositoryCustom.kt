package com.example.jhouse_server.domain.record.repository.retrospection

import com.example.jhouse_server.domain.record.dto.RecordPageCondition
import com.example.jhouse_server.domain.record.dto.RecordThumbnailResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RetrospectionRepositoryCustom {

    fun findRetrospections(condition: RecordPageCondition, pageable: Pageable): Page<RecordThumbnailResDto>
}