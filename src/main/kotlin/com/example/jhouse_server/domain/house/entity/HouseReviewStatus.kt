package com.example.jhouse_server.domain.house.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */
enum class HouseReviewStatus(val value: String) {
    APPLY("신청"),
    APPROVE("승인"),
    REJECT("반려"),
}
/**
 * =============================================================================================
 * ENUM CLASS CONVERTER
 * =============================================================================================
 */
@Converter(autoApply = true)
class HouseReviewStatusConverter: AttributeConverter<HouseReviewStatus, String> {
    override fun convertToDatabaseColumn(attribute: HouseReviewStatus?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): HouseReviewStatus? {
        return HouseReviewStatus.values().firstOrNull {it.name == dbData}
    }

}