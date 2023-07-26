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
    TIME_OUT_EXCEPTION(HttpStatus.BAD_REQUEST, "C0004", "트래픽이 초과되었으므로 잠시 후에 다시 요청해주세요."),

    // Jwt
    UNAUTHORIZED_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J0000", "권한 정보가 없는 토큰입니다."),
    WRONG_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "J0001", "잘못된 JWT 서명입니다."),
    EXPIRE_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J0002", "만료된 JWT 토큰입니다."),
    NOT_SUPPORT_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J0003", "지원되지 않는 JWT 토큰입니다."),
    WRONG_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "J0004", "JWT 토큰이 잘못되었습니다."),
    DONT_VALIDATE_TOKEN(HttpStatus.UNAUTHORIZED, "J0005", "토큰 검증 실패"),
    DONT_HAVE_AUTHORITY(HttpStatus.FORBIDDEN, "J0006", "권한이 없습니다."),

    // User
    EXIST_PHONE_NUM(HttpStatus.BAD_REQUEST, "U0000", "이미 가입된 전화번호입니다."),
    DONT_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "U0001", "존재하지 않는 이메일입니다."),
    DONT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "U0002", "비밀번호가 일치하지 않습니다."),
    EXIST_NICK_NAME(HttpStatus.BAD_REQUEST, "U0005", "이미 존재하는 닉네임입니다."),
    SAME_PASSWORD(HttpStatus.BAD_REQUEST, "U0006", "비밀번호가 같습니다."),
    EXIST_EMAIL(HttpStatus.BAD_REQUEST, "U0007", "이미 가입된 아이디입니다."),
    WITHDRAWAL_WAIT(HttpStatus.BAD_REQUEST, "U0008", "이미 탈퇴신청 되었습니다."),

    // Post
    ALREADY_LOVE(HttpStatus.BAD_REQUEST, "P0000", "이미 좋아요를 한 게시글입니다."),

    // Record
    ALREADY_APPROVE(HttpStatus.BAD_REQUEST, "R0000", "이미 승인처리된 글입니다."),

    // House
    LENGTH_OUT_OF_CONTENTS(HttpStatus.BAD_REQUEST, "H0000", "빈집 게시글의 내용이 10000자를 넘었습니다."),
    DONT_REPORT_HOUSE_MINE(HttpStatus.UNAUTHORIZED, "H0001", "게시글 작성자 본인이 자신의 게시글을 신고할 수 없습니다."),
    ALREADY_SCRAP(HttpStatus.BAD_REQUEST, "H0002", "이미 스크랩한 게시글입니다."),
    DUPLICATE_REPORT(HttpStatus.BAD_REQUEST, "H0003", "이미 신고처리된 게시글입니다."),
}