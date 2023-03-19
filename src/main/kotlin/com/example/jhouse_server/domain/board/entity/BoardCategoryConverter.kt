package com.example.jhouse_server.domain.board.entity

import com.example.jhouse_server.domain.board.PrefixCategory
import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class BoardCategory(val value: String, val superCategory: PrefixCategory) {
    INTERIOR("인테리어", PrefixCategory.ADVERTISEMENT),
    ESTATE("토지", PrefixCategory.ADVERTISEMENT),
    REAL_ESTATE("부동산", PrefixCategory.ADVERTISEMENT),

    QUESTION("질문", PrefixCategory.DEFAULT),
    DAILY("일상", PrefixCategory.DEFAULT),

    TREND("트렌드", PrefixCategory.INTRO),
    REVIEW("후기", PrefixCategory.INTRO),
}

@Converter(autoApply = true)
class BoardCategoryConverter: AttributeConverter<BoardCategory, String> {
    override fun convertToDatabaseColumn(attribute: BoardCategory?): String {
        return attribute?.name ?: BoardCategory.QUESTION.value
    }

    override fun convertToEntityAttribute(dbData: String?): BoardCategory {
        return BoardCategory.values().firstOrNull { it.value == dbData } ?: BoardCategory.QUESTION
    }

}
