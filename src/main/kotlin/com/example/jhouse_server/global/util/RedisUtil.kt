package com.example.jhouse_server.global.util

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisUtil (
        val redisTemplate: RedisTemplate<String, String>
) {
    private val REFRESH_TOKEN_EXPIRE_TIME: Long = 1000 * 60 * 5   //3분

    fun setValues(key: String, value: String) {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        values.set(key, value)
    }

    fun getValues(key: String): String? {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        return values.get(key)
    }

    fun setValuesExpired(key: String, value: String) {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        val expireDuration: Duration = Duration.ofSeconds(REFRESH_TOKEN_EXPIRE_TIME)
        values.set(key, value, expireDuration)
    }

    fun deleteValues(key: String) {
        redisTemplate.delete(key)
    }
}