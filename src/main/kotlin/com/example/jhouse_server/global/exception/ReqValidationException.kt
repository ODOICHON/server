package com.example.jhouse_server.global.exception

class ReqValidationException(
    val fieldMessage : String
) : RuntimeException()