package com.example.jhouse_server.global.interceptor

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogInterceptor : HandlerInterceptor {
    private val LOG_ID = "logId"
    private val logger = LoggerFactory.getLogger(this::class.java)
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        val uuid = UUID.randomUUID().toString();
        request.setAttribute(LOG_ID, uuid);
        return if (request.method == "POST") {
            val inputMap: Map<String, Any> = ObjectMapper().readValue(
                request.inputStream,
                Map::class.java
            ) as Map<String, Any>
            logger.info("요청 정보: $inputMap")
            logger.info("요청 URL: " + request.requestURL)
            true
        } else {
            logger.info("요청 정보: " + request.queryString)
            logger.info("요청 URL: " + request.requestURL)
            true
        }
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        logger.info("postHandle [{}]", modelAndView)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        val requestURI = request.requestURI
        val logId = request.getAttribute(LOG_ID) as String
        logger.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler)
        if (ex != null) {
            logger.error("afterCompletion error!!", ex)
        }
    }
}