package com.example.jhouse_server.global.aop.log.trace

import com.example.jhouse_server.global.aop.log.logger
import com.example.jhouse_server.global.exception.ApplicationException
import org.springframework.stereotype.Component
import java.util.*

@Component
class LogTrace {

    val threadId: ThreadLocal<String> = ThreadLocal()


    fun start(method: String): TraceInfo{
        syncTrace()
        val id = threadId.get()
        val startTime = System.currentTimeMillis()
        logger().info("[${id}] $method ==== start")
        return TraceInfo(id, method, startTime)
    }

    fun end(traceInfo: TraceInfo){
        val endTime = System.currentTimeMillis()
        val resultTime = endTime - traceInfo.startTime
        if (resultTime >= 1000) logger().warn("[${traceInfo.threadId}] ${traceInfo.method} ==== execute time = ${resultTime}ms")
        else logger().info("[${traceInfo.threadId}] ${traceInfo.method} ==== execute time = ${resultTime}ms")
    }

    fun apiException(e: ApplicationException, traceInfo: TraceInfo) {
        logger().error("[${traceInfo.threadId}] ${traceInfo.method} ==== API EXCEPTION! [${e.errorCode.code}] ${e.errorCode.message}")
    }

    fun exception(e: Exception, traceInfo: TraceInfo) {
        logger().error("[${traceInfo.threadId}] ${traceInfo.method} ==== INTERNAL ERROR! ${e.printStackTrace()}")
    }

    private fun syncTrace() {
        val id = threadId.get()
        if (id == null) {
            threadId.set(createThreadId())
        }
    }

    private fun createThreadId(): String {
        return UUID.randomUUID().toString().substring(0,8)
    }
}