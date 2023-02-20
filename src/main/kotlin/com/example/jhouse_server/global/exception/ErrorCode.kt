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

    // Jwt
    UNAUTHORIZED_JWT_TOKEN(HttpStatus.BAD_REQUEST, "J0000", "권한 정보가 없는 토큰입니다."),
    WRONG_JWT_SIGNATURE(HttpStatus.BAD_REQUEST, "J0001", "잘못된 JWT 서명입니다."),
    EXPIRE_JWT_TOKEN(HttpStatus.BAD_REQUEST, "J0002", "만료된 JWT 토큰입니다."),
    NOT_SUPPORT_JWT_TOKEN(HttpStatus.BAD_REQUEST, "J0003", "지원되지 않는 JWT 토큰입니다."),
    WRONG_JWT_TOKEN(HttpStatus.BAD_REQUEST, "J0004", "JWT 토큰이 잘못되었습니다."),
    DONT_VALIDATE_TOKEN(HttpStatus.BAD_REQUEST, "J0005", "토큰 검증 실패"),

    // User
    EXIST_PHONE_NUM(HttpStatus.BAD_REQUEST, "U0000", "이미 가입된 전화번호입니다."),
    DONT_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "U0001", "존재하지 않는 이메일입니다."),
    DONT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "U0002", "비밀번호가 일치하지 않습니다."),
    ALREADY_LOGOUT(HttpStatus.BAD_REQUEST, "U0003", "로그아웃 된 사용자입니다."),
    DONT_MATCH_WITH_TOKEN(HttpStatus.BAD_REQUEST, "U0004", "토큰의 유저 정보가 일치하지 않습니다."),

}