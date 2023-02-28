package com.example.jhouse_server.domain.intro.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class UseCategory(val value: String) {
    POSSIBLE("신청 가능"),
    IMPOSSIBLE("신청 불가능"),
}

@Converter(autoApply = true)
class UseCategoryConverter: AttributeConverter<UseCategory, String> {
    override fun convertToDatabaseColumn(attribute: UseCategory?): String {
        return attribute?.name ?: UseCategory.POSSIBLE.value
    }

    override fun convertToEntityAttribute(dbData: String?): UseCategory {
        return UseCategory.values().firstOrNull{ it.value == dbData } ?: UseCategory.POSSIBLE
    }

}
