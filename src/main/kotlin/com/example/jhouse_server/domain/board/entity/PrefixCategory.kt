package com.example.jhouse_server.domain.board

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class PrefixCategory(val value: String) {
    DEFAULT("자유"),
    INTRO("소개"),
    ADVERTISEMENT("홍보");

    companion object {
        fun fromValue(value: String) : PrefixCategory {
//            println("value is $value")
            return values().firstOrNull {
                it.name == value
            } ?: throw IllegalArgumentException()
        }
    }
}

@Converter(autoApply = true)
class PrefixCategoryConverter : AttributeConverter<PrefixCategory, String> {
    override fun convertToDatabaseColumn(attribute: PrefixCategory?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): PrefixCategory? {
        return if (dbData == null) {
            return null
        } else {
            val result = PrefixCategory.fromValue(dbData)
            println(result)
            return result
        }
    }

}
