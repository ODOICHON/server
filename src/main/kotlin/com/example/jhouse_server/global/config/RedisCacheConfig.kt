package com.example.jhouse_server.global.config

import net.sf.ehcache.Cache
import net.sf.ehcache.config.CacheConfiguration
import net.sf.ehcache.config.DiskStoreConfiguration
import net.sf.ehcache.config.PersistenceConfiguration
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
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
        val configuration = net.sf.ehcache.config.Configuration()
        // path는 DiskStoreConfiguration 클래스의 ENV enum 참조하거나 PhysicalPath로 설정
        configuration.diskStore(DiskStoreConfiguration().path("java.io.tmpdir"))
        val manager = net.sf.ehcache.CacheManager.create(configuration)

        // 기존 캐시 제거
        val cacheManager = EhCacheCacheManager(manager)
        cacheManager.afterPropertiesSet()
        cacheManager.cacheManager?.getCache("getCache")?.let { cache ->
            manager.removeCache(cache.name)
        }

        // 새로운 캐시 생성
        val getCacheConfig: CacheConfiguration = CacheConfiguration()
            .maxEntriesLocalHeap(1000) // 로컬 힙 메모리에서 최대로 사용할 수 있는 값
            .maxEntriesLocalDisk(10000)
            .eternal(false) // false로 지정해야 TTL 적용가능
            .timeToIdleSeconds(1800) // 지정 시간 동안 조회 되지 않으면 삭제 ( 30분 )
            .timeToLiveSeconds(1800) // 지정 시간 이후 삭제
            .memoryStoreEvictionPolicy("LRU") // 가장 오랫동안 참조되지 않은 것부터 제거
            .transactionalMode(CacheConfiguration.TransactionalMode.OFF) // 트랜잭션에 따라 참조 호출 발생으로 성능 저하 가능성이 있어 OFF
            .persistence(PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP)) // temp diskstore 사용
            .name("getCache") // 기본 캐시 이름
        val getAuthenticatedMenuByUriCache = Cache(getCacheConfig)

        // 캐시 추가
        manager.addCache(getAuthenticatedMenuByUriCache)
        return EhCacheCacheManager(manager)
    }
}