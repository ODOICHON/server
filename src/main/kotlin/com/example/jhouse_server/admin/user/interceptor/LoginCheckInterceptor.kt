package com.example.jhouse_server.admin.user.interceptor

import com.example.jhouse_server.admin.user.SessionConst
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginCheckInterceptor : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val requestURI = request.requestURI
        // 세션 존재 x -> 생성 x
        val session = request.getSession(false)
        if (session?.getAttribute(SessionConst.LOGINUSER) == null) {
            response.sendRedirect("/admin?redirectURI=$requestURI")
            // 더 이상 호출 x
            return false
        }
        return true
    }
}