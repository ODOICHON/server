package com.example.jhouse_server.domain.ads.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class AdsPostCategory(val value: String) {
    INTERIOR("인테리어"),
    ESTATE("토지"),
    REAL_ESTATE("부동산"),
}

@Converter(autoApply = true)
class AdsPostCategoryConverter: AttributeConverter<AdsPostCategory, String> {
    override fun convertToDatabaseColumn(attribute: AdsPostCategory?): String {
        return attribute?.name ?: AdsPostCategory.INTERIOR.value
    }

    override fun convertToEntityAttribute(dbData: String?): AdsPostCategory {
        return AdsPostCategory.values().firstOrNull { it.value == dbData } ?: AdsPostCategory.INTERIOR
    }

}