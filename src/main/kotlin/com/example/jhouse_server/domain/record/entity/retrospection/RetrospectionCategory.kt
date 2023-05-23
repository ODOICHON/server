package com.example.jhouse_server.domain.record.entity.retrospection

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class RetrospectionCategory(val value: String) {
    RETROSPECTION("회고");

    companion object {
        fun getCategory(value: String): RetrospectionCategory {
            for(category in RetrospectionCategory.values()) {
                if(category.value == value) {
                    return category
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }

        fun getCategoryByEnum(value: String): RetrospectionCategory {
            for(category in RetrospectionCategory.values()) {
                if(category.toString() == value.uppercase()) {
                    return category
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}