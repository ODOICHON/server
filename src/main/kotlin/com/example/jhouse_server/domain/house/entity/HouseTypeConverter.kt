package com.example.jhouse_server.domain.house.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter
/**
 * =============================================================================================
 * ENUM CLASS CONVERTER
 * =============================================================================================
 */
@Converter(autoApply = true)
class RentalTypeConverter : AttributeConverter<RentalType, String> {
    override fun convertToDatabaseColumn(attribute: RentalType?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): RentalType? {
        return RentalType.values().firstOrNull {it.name == dbData}
    }

}
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */
enum class RentalType(val value: String) {
    SALE("매매"),
    JEONSE("전세"),
    MONTHLYRENT("월세"),
    ;
}