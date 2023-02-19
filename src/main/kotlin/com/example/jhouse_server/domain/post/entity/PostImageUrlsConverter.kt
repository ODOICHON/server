package com.example.jhouse_server.domain.post.entity

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class PostImageUrlsConverter : AttributeConverter<List<String>, String?> {
    private val splitStr = ","
    override fun convertToDatabaseColumn(attribute: List<String>): String? {
        return if(attribute.isNotEmpty()) {
            attribute.joinToString(splitStr)
        } else {
            null
        }
    }

    override fun convertToEntityAttribute(dbData: String?): List<String> {
        return dbData?.split(splitStr) ?: emptyList()
    }
}
