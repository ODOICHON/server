package com.example.jhouse_server.global.exception

class ApplicationException(
        val errorCode: ErrorCode
) : RuntimeException()