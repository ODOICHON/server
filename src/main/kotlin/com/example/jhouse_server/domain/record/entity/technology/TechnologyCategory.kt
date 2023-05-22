package com.example.jhouse_server.domain.record.entity.technology

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class TechnologyCategory(val value: String) {
    DISASTER("장애 관리"),
    ISSUE("이슈 관리"),
    NEW_TECH("신 기술"),
    ARCHITECTURE("설계");

    companion object {
        fun getCategory(value: String): TechnologyCategory {
            for(category in TechnologyCategory.values()) {
                if(category.value == value) {
                    return category
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }

        fun getCategoryByEnum(value: String): TechnologyCategory {
            for(category in TechnologyCategory.values()) {
                if(category.toString() == value.uppercase()) {
                    return category
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}