package com.example.jhouse_server.domain.house.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter
/**
 * =============================================================================================
 * ENUM CLASS CONVERTER
 * =============================================================================================
 */
@Converter(autoApply = true)
class DealStateConverter : AttributeConverter<DealState, String> {
    override fun convertToDatabaseColumn(attribute: DealState?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): DealState? {
        return DealState.values().firstOrNull {it.name == dbData}
    }
}
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */
enum class DealState(val value : String) {
    APPLYING("승인중"),
    ONGOING("판매중"),
    COMPLETED("판매완료"),
    REJECTED("반려")
    ;

    companion object {
        fun from(dealState: DealState): String {
            return dealState.value
            }
        }
    }
