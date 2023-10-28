package com.example.jhouse_server.admin.auth.dto
/**
 * =============================================================================================
 * LoginForm                -- 로그인 요청 DTO
 * email                    -- 이메일 계정
 * password                 -- 비밀번호
 * =============================================================================================
 */
data class LoginForm(
        val email: String?,
        val password: String?
)