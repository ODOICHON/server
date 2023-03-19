package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.global.util.AesUtil
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class CryptoConverter(
        private val aesUtil: AesUtil
): AttributeConverter<String, String> {

    override fun convertToDatabaseColumn(attribute: String?): String {
        return aesUtil.encrypt(attribute!!)
    }

    override fun convertToEntityAttribute(dbData: String?): String {
        return aesUtil.decrypt(dbData!!)
    }
}