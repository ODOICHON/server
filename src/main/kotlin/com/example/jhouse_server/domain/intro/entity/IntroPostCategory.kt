package com.example.jhouse_server.domain.intro.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class IntroPostCategory(val value: String) {
    TREND("트렌드"),
    REVIEW("후기"),

}

@Converter(autoApply = true)
class IntroPostCategoryConverter : AttributeConverter<IntroPostCategory, String> {
    override fun convertToDatabaseColumn(attribute: IntroPostCategory?): String {
        return attribute?.name ?: IntroPostCategory.TREND.value
    }

    override fun convertToEntityAttribute(dbData: String?): IntroPostCategory {
        return IntroPostCategory.values().firstOrNull{ it.value == dbData } ?: IntroPostCategory.TREND
    }

}