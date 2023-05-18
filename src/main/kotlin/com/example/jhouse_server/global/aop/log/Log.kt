package com.example.jhouse_server.global.aop.log

import org.slf4j.LoggerFactory


inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!


class Log {
}