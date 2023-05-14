package com.example.jhouse_server.domain.record_category.service

import com.example.jhouse_server.domain.record_category.dto.RecordCategoryReqDto
import com.example.jhouse_server.domain.record_category.dto.RecordCategoryResDto
import com.example.jhouse_server.domain.record_category.dto.TemplateUpdateReqDto

interface RecordCategoryService {

    fun getTemplate(recordCategoryReqDto: RecordCategoryReqDto): RecordCategoryResDto

    fun updateTemplate(templateUpdateReqDto: TemplateUpdateReqDto)
}