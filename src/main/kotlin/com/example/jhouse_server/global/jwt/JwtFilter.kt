package com.example.jhouse_server.global.jwt

import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter (
        private val tokenProvider: TokenProvider
): OncePerRequestFilter() {

    private val AUTHORIZATION_HEADER: String = "Authorization"

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        if (!request.requestURI.startsWith("/api/v1/users") ||
                request.requestURI.startsWith("/api/v1/users/logout")) {
            val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
            val jwt: String = tokenProvider.resolveToken(bearerToken).toString()

            if (StringUtils.hasText(jwt)) {
                tokenProvider.validateToken(jwt)
            }

            filterChain.doFilter(request, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }
}