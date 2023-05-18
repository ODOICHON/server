package com.example.jhouse_server.global.aop.log

import com.example.jhouse_server.global.aop.log.trace.LogTrace
import com.example.jhouse_server.global.aop.log.trace.TraceInfo
import com.example.jhouse_server.global.exception.ApplicationException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class LogAspect (
        var logTrace: LogTrace
        ){


    @Around("com.example.jhouse_server.global.aop.log.Pointcuts.allService()")
    fun executingTimeLog(joinPoint: ProceedingJoinPoint): Any? {
        var traceInfo: TraceInfo? = null
        try {
            traceInfo = logTrace.start(joinPoint.signature.toShortString())
            val result = joinPoint.proceed()
            logTrace.end(traceInfo)
            return result
        } catch (e: ApplicationException) {
            traceInfo?.let { logTrace.apiException(e, it) }
            throw e
        } catch (e: Exception) {
            traceInfo?.let { logTrace.exception(e, it) }
            throw e
        }
    }


}
