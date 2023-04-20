package com.example.jhouse_server.global.bucket;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitService {

    private Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    private Refill createRefill(long tokens, long seconds) {
        return Refill.greedy(tokens, Duration.ofSeconds(seconds));
    }

    private Bucket createBucket(long capacity, long tokens, long seconds) {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(capacity, createRefill(tokens, seconds)))
                .build();
    }

    public Bucket resolveHttpBucket(HttpServletRequest request) {
        String key = getClientIp(request);
        Bucket value = cache.get(key);
        if (value == null) {
            value = createBucket(10000, 10000, 60);
            cache.put(key, value);
        }
        return value;
    }

    public Bucket resolveSmsBucket(HttpServletRequest request) {
        String key = getClientIp(request);
        Bucket value = cache.get(key);
        if (value == null) {
            value = createBucket(100, 100, 60);
            cache.put(key, value);
        }
        return value;
    }
}
