package com.example.jhouse_server.domain.house.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class DealStateConverter : AttributeConverter<DealState, String> {
    override fun convertToDatabaseColumn(attribute: DealState?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): DealState? {
        return DealState.values().firstOrNull {it.name == dbData}
    }
}

enum class DealState {
    ONGOING,
    COMPLETED,
}