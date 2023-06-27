package com.example.jhouse_server.domain.user.entity.agent

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class Estate(val value: String) {
    APARTMENT("아파트"),
    HOUSE("주택"),
    FARM("농가");

    companion object {
        fun getEstate(value: String): Estate {
            for(estate in Estate.values()) {
                if(estate.value == value) {
                    return estate
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}