package com.example.jhouse_server.global.util

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class RedisUtil (
        val redisTemplate: RedisTemplate<String, String>
) {
    fun setValues(key: String, value: String) {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        values.set(key, value)
    }

    fun getValues(key: String): String? {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        return values.get(key)
    }

    fun setValuesExpired(key: String, value: String, time: Long) {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        val expireDuration: Duration = Duration.ofSeconds(time)
        values.set(key, value, expireDuration)
    }

    fun deleteValues(key: String) {
        redisTemplate.delete(key)
    }
}