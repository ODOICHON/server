package com.example.jhouse_server.global.jwt

import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import com.example.jhouse_server.global.exception.ErrorCode.*
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.Key
import java.util.*

@Component
class TokenProvider {

    private val ACCESS_TOKEN_EXPIRE_TIME: Long = 1000 * 60 * 30     //30분

    private val REFRESH_TOKEN_EXPIRE_TIME: Long = 1000 * 60 * 60 * 24 * 7   //7일

    private val AUTHORITIES_KEY: String = "auth"

    private val BEARER_PREFIX: String = "Bearer "

    private val key: Key

    constructor(@Value("\${spring.jwt.key}") secretKey: String) {
        val keyBytes = Base64.getDecoder().decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createTokenResponse(user: User): TokenDto {
        val now = Date()

        val accessToken = BEARER_PREFIX + Jwts.builder()
                .setSubject(user.email)
                .setExpiration(Date(now.time + ACCESS_TOKEN_EXPIRE_TIME))
                .claim(AUTHORITIES_KEY, user.authority)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact()
        val refreshToken = Jwts.builder()
                .setSubject(user.email)
                .setExpiration(Date(now.time + REFRESH_TOKEN_EXPIRE_TIME))
                .claim(AUTHORITIES_KEY, user.authority)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact()

        return TokenDto(accessToken, refreshToken)
    }

    fun resolveToken(bearerToken: String): String? {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7)
        }

        return null
    }

    fun validateToken(token: String) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

            if (claims[AUTHORITIES_KEY] == null) {
                ApplicationException(UNAUTHORIZED_JWT_TOKEN)
            }
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            ApplicationException(WRONG_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            ApplicationException(WRONG_JWT_SIGNATURE)
        } catch (e: ExpiredJwtException) {
            ApplicationException(EXPIRE_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            ApplicationException(NOT_SUPPORT_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            ApplicationException(WRONG_JWT_TOKEN)
        }
    }

    fun getSubject(token: String): String {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

        return claims.subject
    }
}