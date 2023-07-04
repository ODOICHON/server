package com.example.jhouse_server.global.config

import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration


@EnableCaching
@Configuration
class RedisCacheConfig {

    @Bean
    @Primary
    fun cacheManager(cf: RedisConnectionFactory?): CacheManager? {
        val redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofHours(3L))
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(cf!!).cacheDefaults(redisCacheConfiguration).build()
    }
    @Bean
    fun ehCacheCacheManager(): EhCacheCacheManager {
        return EhCacheCacheManager(ehCacheCacheManagerBean()!!.`object` as net.sf.ehcache.CacheManager)
    }
    @Bean
    fun ehCacheCacheManagerBean(): EhCacheManagerFactoryBean? {
        val factoryBean = EhCacheManagerFactoryBean()
        factoryBean.setConfigLocation(ClassPathResource("ehcache.xml"))
        factoryBean.setShared(true)
        return factoryBean
    }

}