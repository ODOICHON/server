package com.example.jhouse_server.domain.board.entity

import com.example.jhouse_server.domain.board.PrefixCategory
import javax.persistence.AttributeConverter
import javax.persistence.Converter
/**
 * =============================================================================================
 *  ENUM CLASS
 * =============================================================================================
 * */
enum class BoardCategory(val value: String, val superCategory: PrefixCategory) {
    INTERIOR("인테리어", PrefixCategory.ADVERTISEMENT),
    ESTATE("토지", PrefixCategory.ADVERTISEMENT),
    REAL_ESTATE("부동산", PrefixCategory.ADVERTISEMENT),

    QUESTION("질문", PrefixCategory.DEFAULT),
    DAILY("일상", PrefixCategory.DEFAULT),
    SHARING("나눔", PrefixCategory.DEFAULT),

    TREND("트렌드", PrefixCategory.INTRO),
    REVIEW("후기", PrefixCategory.INTRO),

    EMPTY("태그 없음", PrefixCategory.ALL),
    ;
}
/**
 * =============================================================================================
 *  ENUM CONVERTER
 * =============================================================================================
 * */
@Converter(autoApply = true)
class BoardCategoryConverter : AttributeConverter<BoardCategory, String> {
    override fun convertToDatabaseColumn(attribute: BoardCategory?): String? {
        return attribute?.name
    }

    override fun convertToEntityAttribute(dbData: String?): BoardCategory? {
        return BoardCategory.values().firstOrNull {it.name == dbData }
    }
}
