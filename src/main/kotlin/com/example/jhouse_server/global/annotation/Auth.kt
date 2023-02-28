package com.example.jhouse_server.global.annotation

import com.example.jhouse_server.domain.user.entity.Authority

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Auth(
        val auth: Authority = Authority.USER
)
