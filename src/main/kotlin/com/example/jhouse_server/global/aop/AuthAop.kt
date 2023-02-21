package com.example.jhouse_server.global.aop

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode.DONT_VALIDATE_TOKEN
import com.example.jhouse_server.global.jwt.TokenProvider
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@Aspect
@Component
class AuthAop (
        val tokenProvider: TokenProvider
) {

    private val AUTHORIZATION_HEADER: String = "Authorization"

    @Pointcut("execution(* com.example.jhouse_server..*.*(..))")
    private fun cut() {}

    @Pointcut("@annotation(com.example.jhouse_server.global.annotation.Auth)")
    private fun enableAuth() {}

    @Before("cut() && enableAuth()")
    public fun before(joinPoint: JoinPoint) {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val bearerToken: String = request.getHeader(AUTHORIZATION_HEADER) ?: throw ApplicationException(DONT_VALIDATE_TOKEN)
        val jwt: String = tokenProvider.resolveToken(bearerToken) ?: throw ApplicationException(DONT_VALIDATE_TOKEN)

        tokenProvider.validateToken(jwt)
    }
}