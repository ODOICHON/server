package com.example.jhouse_server.global.interceptor

import com.example.jhouse_server.global.bucket.RateLimitService
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import io.github.bucket4j.Bucket
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class SmsInterceptor(
    private val rateLimitService: RateLimitService
): HandlerInterceptor {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val bucket: Bucket = rateLimitService.resolveSmsBucket(request)
        log.info("=========== Before Method ===========")
        log.info("접속 ip 주소 : {}", getClientIp(request))

        return if(bucket.tryConsume(1)) {
            true
        } else {
            log.info("트래픽 초과, 접속 ip 주소 : {}", getClientIp(request))
            throw ApplicationException(ErrorCode.TIME_OUT_EXCEPTION)
        }
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        log.info("=========== Method Executed ===========")
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        log.info("=========== Method Completed ===========")
    }

    private fun getClientIp(request: HttpServletRequest): String {
        var ip = request.getHeader("X-Forwarded-For")

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("Proxy-Client-IP")
        }

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("WL-Proxy-Client-IP")
        }

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("HTTP_CLIENT_IP")
        }

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR")
        }

        if (ip == null || ip.isEmpty()) {
            ip = request.remoteAddr
        }

        return ip!!
    }
}