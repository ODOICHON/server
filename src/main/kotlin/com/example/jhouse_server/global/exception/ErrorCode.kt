package com.example.jhouse_server.global.exception

import org.springframework.http.HttpStatus

enum class ErrorCode(
        val status: HttpStatus,
        val code: String,
        val message: String
) {
    // Common
    INTERNAL_SERVER_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "C0000", "예기치 못한 오류가 발생했습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.NOT_FOUND, "C0001", "존재하지 않는 리소스 요청입니다."),
    INVALID_VALUE_EXCEPTION(HttpStatus.BAD_REQUEST, "C0002", "올바르지 않은 요청 값입니다."),
    UNAUTHORIZED_EXCEPTION(HttpStatus.UNAUTHORIZED, "C0003", "해당 요청에 대한 권한이 없습니다."),
}