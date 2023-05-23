package com.example.jhouse_server.domain.record_review.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class RecordReviewStatus(val value: String) {
    APPROVE("approve"),
    REJECT("reject"),
    MINE("mine");

    companion object {
        fun getStatus(value: String): RecordReviewStatus {
            for(status in RecordReviewStatus.values()) {
                if(status.value == value.lowercase()) {
                    return status
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}