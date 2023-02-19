package com.example.jhouse_server.domain.post.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class PostCategory(val value: String) {
    QUESTION("질문"),
    DAILY("일상"),
}

@Converter(autoApply = true)
class PostCategoryConverter: AttributeConverter<PostCategory, String> {
    override fun convertToDatabaseColumn(attribute: PostCategory?): String {
        return attribute?.name ?: PostCategory.DAILY.value
    }

    override fun convertToEntityAttribute(dbData: String?): PostCategory {
        return PostCategory.values().firstOrNull{ it.value == dbData } ?: PostCategory.DAILY
    }

}