package com.example.jhouse_server.domain.record_category.service

import com.example.jhouse_server.domain.record_category.dto.RecordCategoryReqDto
import com.example.jhouse_server.domain.record_category.dto.RecordCategoryResDto
import com.example.jhouse_server.domain.record_category.dto.TemplateUpdateReqDto
import com.example.jhouse_server.domain.record_category.entity.RecordCategory
import com.example.jhouse_server.domain.record_category.entity.RecordCategoryEnum
import com.example.jhouse_server.domain.record_category.repository.RecordCategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecordCategoryServiceImpl(
    val recordCategoryRepository: RecordCategoryRepository
): RecordCategoryService {

    override fun getTemplate(recordCategoryReqDto: RecordCategoryReqDto): RecordCategoryResDto {
        val category = RecordCategoryEnum.getRecordCategoryEnum(recordCategoryReqDto.category)!!
        val recordCategoryOptional = recordCategoryRepository.findByCategory(category)

        return if(recordCategoryOptional.isEmpty) {
            RecordCategoryResDto("", category.value)
        } else {
            val recordCategory = recordCategoryOptional.get()
            RecordCategoryResDto(recordCategory.template, recordCategory.category.value)
        }
    }

    @Transactional
    override fun updateTemplate(templateUpdateReqDto: TemplateUpdateReqDto) {
        val category = RecordCategoryEnum.getRecordCategoryEnum(templateUpdateReqDto.category)!!
        val template = templateUpdateReqDto.template
        val recordCategoryOptional = recordCategoryRepository.findByCategory(category)

        if(recordCategoryOptional.isEmpty) {
            val recordCategory = RecordCategory(template, category)
            recordCategoryRepository.save(recordCategory)
        } else {
            val recordCategory = recordCategoryOptional.get()
            recordCategory.template = template
        }
    }
}