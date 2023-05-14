package com.example.jhouse_server.domain.record_category.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode

enum class RecordCategoryEnum(val value: String) {
    CULTURE("팀 내 문화"),
    DISASTER("장애 관리"),
    ISSUE("이슈 관리"),
    NEW_TECH("신 기술"),
    ARCHITECTURE("설계"),
    RETROSPECTION("회고");

    companion object {
        fun getRecordCategoryEnum(value: String): RecordCategoryEnum? {
            for(recordCategoryEnum in RecordCategoryEnum.values()) {
                if(recordCategoryEnum.value == value) {
                    return recordCategoryEnum
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}