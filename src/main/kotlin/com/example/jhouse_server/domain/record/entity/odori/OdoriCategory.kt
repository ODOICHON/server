package com.example.jhouse_server.domain.record.entity.odori

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class OdoriCategory(val value: String) {
    CULTURE("팀 내 문화");

    companion object {
        fun getCategory(value: String): OdoriCategory {
            for(category in OdoriCategory.values()) {
                if(category.value == value) {
                    return category
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }

        fun getCategoryByEnum(value: String): OdoriCategory {
            for(category in OdoriCategory.values()) {
                if(category.toString() == value.uppercase()) {
                    return category
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}