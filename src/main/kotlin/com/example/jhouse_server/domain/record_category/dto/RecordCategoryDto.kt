package com.example.jhouse_server.domain.record_category.dto

data class RecordCategoryResDto (
    val template: String,
    val category: String
)

data class TemplateUpdateReqDto (
    val template: String,
    val category: String
)

data class RecordCategoryReqDto (
    val category: String
)