package com.example.jhouse_server.domain.house.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */
enum class ReportType(val value: String) {
    FAKE_SALE("허위매물"),
    ADVERTISING_BOARD("스팸게시글/광고"),
    ETC("기타"),
}
/**
 * =============================================================================================
 * ENUM CLASS CONVERTER
 * =============================================================================================
 */
@Converter(autoApply = true)
class ReportTypeConverter : AttributeConverter<ReportType, String> {
    override fun convertToDatabaseColumn(attribute: ReportType?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): ReportType? {
        return ReportType.values().firstOrNull { it.name == dbData }
    }

}