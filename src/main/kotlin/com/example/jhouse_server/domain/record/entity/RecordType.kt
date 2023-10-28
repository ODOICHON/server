package com.example.jhouse_server.domain.record.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
/**
 * =============================================================================================
 * ENUM CLASS
 * =============================================================================================
 */
enum class RecordType(val value: String) {
    ALL("all"),
    ODORI("odori"),
    RETRO("retro"),
    TECH("tech");

    companion object {
        fun getType(value: String): RecordType {
            for(type in RecordType.values()) {
                if(type.value == value.lowercase()) {
                    return type
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}