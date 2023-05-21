package com.example.jhouse_server.domain.record_category.service

import com.example.jhouse_server.domain.record_category.entity.RecordCategoryEnum
import com.example.jhouse_server.domain.record_category.repository.RecordCategoryRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class RecordCategoryServiceImplTest @Autowired constructor(
    private val recordCategoryService: RecordCategoryService,
    private val recordCategoryRepository: RecordCategoryRepository
) {

    private val templateSaveReqDto = MockEntity.templateSaveReqDto()
    private val templateUpdateReqDto = MockEntity.templateUpdateReqDto()

    @Test
    @DisplayName("템플릿 저장 테스트")
    fun saveTemplateTest() {
        //given

        //when
        recordCategoryService.updateTemplate(templateSaveReqDto)
        val category = RecordCategoryEnum.getRecordCategoryEnum(templateSaveReqDto.category)
        val findCategory = recordCategoryRepository.findByCategory(category)

        //then
        assertThat(findCategory).isPresent
        assertThat(findCategory.get().template).isEqualTo(templateSaveReqDto.template)
    }

    @Test
    @DisplayName("템플릿 수정 테스트")
    fun updateTemplateTest() {
        //given
        recordCategoryService.updateTemplate(templateSaveReqDto)

        //when
        recordCategoryService.updateTemplate(templateUpdateReqDto)
        val category = RecordCategoryEnum.getRecordCategoryEnum(templateUpdateReqDto.category)
        val findCategory = recordCategoryRepository.findByCategory(category)

        //then
        assertThat(findCategory).isPresent
        assertThat(findCategory.get().template).isEqualTo(templateUpdateReqDto.template)
    }

    @Test
    @DisplayName("템플릿 조회 테스트 - 등록X")
    fun getEmptyTemplateTest() {
        //given

        //when
        val recordCategoryResDto = recordCategoryService.getTemplate(templateSaveReqDto.category)

        //then
        assertThat(recordCategoryResDto.template).isBlank
    }

    @Test
    @DisplayName("템플릿 조회 테스트 - 등록O")
    fun getTemplateTest() {
        //given
        recordCategoryService.updateTemplate(templateSaveReqDto)

        //when
        val recordCategoryResDto = recordCategoryService.getTemplate(templateSaveReqDto.category)

        //then
        assertThat(recordCategoryResDto.template).isEqualTo(templateSaveReqDto.template)
    }
}