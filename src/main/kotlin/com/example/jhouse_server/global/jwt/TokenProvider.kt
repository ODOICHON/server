package com.example.jhouse_server.global.jwt

import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.global.exception.ApplicationException
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

    private val USER_TYPE_KEY: String = "type"

    private val BEARER_PREFIX: String = "Bearer "

    private val key: Key

    constructor(@Value("\${spring.jwt.key}") secretKey: String) {
        val keyBytes = Base64.getDecoder().decode(secretKey)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun createTokenResponse(user: User): TokenDto {
        val now = Date()

        val accessToken = BEARER_PREFIX + Jwts.builder()
                .setSubject(user.userName)
                .setExpiration(Date(now.time + ACCESS_TOKEN_EXPIRE_TIME))
                .claim(AUTHORITIES_KEY, user.authority)
                .claim(USER_TYPE_KEY, user.userType)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact()

        return TokenDto(accessToken)
    }

    fun createRefreshToken(): String {
        val now = Date()

        return Jwts.builder()
            .setExpiration(Date(now.time + REFRESH_TOKEN_EXPIRE_TIME))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun resolveToken(bearerToken: String): String? {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7)
        }

        return null
    }

    fun validateToken(token: String, access: Boolean) {
        try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

            if (access && claims[AUTHORITIES_KEY] == null) {
                throw ApplicationException(UNAUTHORIZED_JWT_TOKEN)
            }
        } catch (e: io.jsonwebtoken.security.SecurityException) {
            throw ApplicationException(WRONG_JWT_SIGNATURE)
        } catch (e: MalformedJwtException) {
            throw ApplicationException(WRONG_JWT_SIGNATURE)
        } catch (e: ExpiredJwtException) {
            throw ApplicationException(EXPIRE_JWT_TOKEN)
        } catch (e: UnsupportedJwtException) {
            throw ApplicationException(NOT_SUPPORT_JWT_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw ApplicationException(WRONG_JWT_TOKEN)
        }
    }

    fun getSubject(token: String): String {
        return try {
            val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body
            claims.subject
        } catch (e: ExpiredJwtException) {
            e.claims.subject
        }
    }

    fun getAuthority(token: String): Authority {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

        return Authority.valueOf(claims[AUTHORITIES_KEY].toString())
    }

    fun getType(token: String): UserType {
        val claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).body

        return UserType.valueOf(claims[USER_TYPE_KEY].toString())
    }
}