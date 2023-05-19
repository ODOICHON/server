package com.example.jhouse_server.domain.record_category.dto

import javax.validation.constraints.NotBlank

data class RecordCategoryResDto (
    val template: String,
    val category: String
)

data class TemplateUpdateReqDto (
    val template: String,
    @field:NotBlank
    val category: String
)