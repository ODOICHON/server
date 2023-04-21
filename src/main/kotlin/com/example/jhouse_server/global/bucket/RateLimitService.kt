package com.example.jhouse_server.global.bucket

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest

@Component
class RateLimitService {

    private val cache: MutableMap<String, Bucket?> = ConcurrentHashMap()

    fun getClientIp(request: HttpServletRequest): String {
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

    private fun getHost(request: HttpServletRequest): String {
        return request.remoteHost
    }

    private fun createRefill(tokens: Long, seconds: Long): Refill {
        return Refill.greedy(tokens, Duration.ofSeconds(seconds))
    }

    private fun createBucket(capacity: Long, tokens: Long, seconds: Long): Bucket {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(capacity, createRefill(tokens, seconds)))
            .build()
    }

    fun resolveHttpBucket(request: HttpServletRequest): Bucket {
        val key = getHost(request)
        var value = cache[key]
        if (value == null) {
            value = createBucket(10000, 10000, 60)
            cache[key] = value
        }
        return value
    }

    fun resolveSmsBucket(request: HttpServletRequest): Bucket {
        val key = getHost(request)
        var value = cache[key]
        if (value == null) {
            value = createBucket(100, 100, 60)
            cache[key] = value
        }
        return value
    }
}