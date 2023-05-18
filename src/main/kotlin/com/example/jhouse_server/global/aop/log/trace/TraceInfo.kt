package com.example.jhouse_server.global.aop.log.trace

data class TraceInfo(
        val threadId: String,
        val method: String,
        val startTime: Long
)
