package com.example.jhouse_server.domain.record_category.controller

import com.example.jhouse_server.domain.record_category.dto.RecordCategoryResDto
import com.example.jhouse_server.domain.record_category.dto.TemplateUpdateReqDto
import com.example.jhouse_server.domain.record_category.service.RecordCategoryService
import com.example.jhouse_server.domain.user.entity.Authority.ADMIN
import com.example.jhouse_server.global.annotation.Auth
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/record_category")
class RecordCategoryController(
    private val recordCategoryService: RecordCategoryService
) {

    @Auth(ADMIN)
    @GetMapping("/template/{category}")
    fun getTemplate(
        @PathVariable("category") category: String
    ): ApplicationResponse<RecordCategoryResDto> {
        return ApplicationResponse.ok(recordCategoryService.getTemplate(category))
    }

    @Auth(ADMIN)
    @PostMapping("/template")
    fun updateTemplate(
        @Validated @RequestBody templateUpdateReqDto: TemplateUpdateReqDto
    ): ApplicationResponse<Nothing> {
        recordCategoryService.updateTemplate(templateUpdateReqDto)
        return ApplicationResponse.ok()
    }
}