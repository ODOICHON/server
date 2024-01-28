package com.example.jhouse_server.global.config

import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
class RequestServletFilter : Filter {
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val wrappedRequest: HttpServletRequest =
            RequestServletWrapper(request as HttpServletRequest)

        chain.doFilter(wrappedRequest, response)
    }
}