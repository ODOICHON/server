package com.example.jhouse_server.domain.board

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class PrefixCategory(val value: String) {
    DEFAULT("자유"),
    INTRO("소개"),
    ADVERTISEMENT("홍보"),
    ALL("공통");

}

@Converter(autoApply = true)
class PrefixCategoryConverter : AttributeConverter<PrefixCategory, String> {
    override fun convertToDatabaseColumn(attribute: PrefixCategory?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): PrefixCategory? {
        return PrefixCategory.values().firstOrNull { it.name == dbData }
    }

}
