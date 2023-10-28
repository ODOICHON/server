package com.example.jhouse_server.domain.record.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */
enum class Part(val value: String) {
    ALL("all"),
    WEB("web"),
    SERVER("server"),
    INFRA("infra");

    companion object {
        fun getPart(value: String): Part {
            for(part in Part.values()) {
                if(part.value == value.lowercase()) {
                    return part
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}