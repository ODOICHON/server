package com.example.jhouse_server.domain.record.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class Part(val value: String) {
    WEB("web"),
    SERVER("server"),
    INFRA("infra");

    companion object {
        fun getPart(value: String): Part? {
            for(part in Part.values()) {
                if(part.value == value) {
                    return part
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}