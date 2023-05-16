package com.example.jhouse_server.domain.record_review_apply.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class RecordReviewApplyStatus(val value: String) {
    APPROVE("approve"),
    WAIT("wait"),
    REJECT("reject"),
    MINE("mine");

    companion object {
        fun getStatus(value: String): RecordReviewApplyStatus {
            for(status in RecordReviewApplyStatus.values()) {
                if(status.value == value.lowercase()) {
                    return status
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}