package com.example.jhouse_server.domain.record_category.repository

import com.example.jhouse_server.domain.record_category.entity.RecordCategory
import com.example.jhouse_server.domain.record_category.entity.RecordCategoryEnum
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RecordCategoryRepository: JpaRepository<RecordCategory, Long> {

    fun findByCategory(category: RecordCategoryEnum): Optional<RecordCategory>
}