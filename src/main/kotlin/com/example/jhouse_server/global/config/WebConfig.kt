package com.example.jhouse_server.global.config

import com.example.jhouse_server.admin.user.interceptor.LoginCheckInterceptor
import com.example.jhouse_server.global.resolver.AuthUserResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
//@EnableWebMvc
class WebConfig (
        val authUserResolver: AuthUserResolver
): WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(authUserResolver)
    }
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**", "/admin", "/css/**", "/error")
    }
}