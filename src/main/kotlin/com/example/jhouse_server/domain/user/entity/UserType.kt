package com.example.jhouse_server.domain.user.entity

enum class UserType(val authority: Authority) {
    WEB(Authority.ADMIN),
    SERVER(Authority.ADMIN);
}