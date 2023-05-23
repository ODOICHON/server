package com.example.jhouse_server.domain.record.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class RecordStatus(val value: String) {
    APPROVE("approve"),
    WAIT("wait"),
    REJECT("reject");

    companion object {
        fun getStatus(value: String): RecordStatus {
            for(status in RecordStatus.values()) {
                if(status.value == value.lowercase()) {
                    return status
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}