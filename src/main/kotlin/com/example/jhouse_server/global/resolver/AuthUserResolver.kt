package com.example.jhouse_server.global.resolver

import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.global.annotation.AuthUser
import com.example.jhouse_server.global.jwt.TokenProvider
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class AuthUserResolver (
        val userRepository: UserRepository,
        val tokenProvider: TokenProvider
): HandlerMethodArgumentResolver {

    private val AUTHORIZATION_HEADER: String = "Authorization"

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        val hasAnnotation: Boolean = parameter.hasParameterAnnotation(AuthUser::class.java)
        val isUserType: Boolean = User::class.java.isAssignableFrom(parameter.parameterType)

        return hasAnnotation && isUserType
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?,
                                 webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): Any? {
        val bearerToken: String = webRequest.getHeader(AUTHORIZATION_HEADER).toString()
        val jwt: String = tokenProvider.resolveToken(bearerToken).toString()
        val email: String = tokenProvider.getSubject(jwt)

        return userRepository.findByEmail(email).orElseThrow()
    }
}