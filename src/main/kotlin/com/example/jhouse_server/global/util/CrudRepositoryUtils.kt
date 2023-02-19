package com.example.jhouse_server.global

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.findByIdOrNull

fun<T, ID> CrudRepository<T, ID>.findByIdOrThrow(id : ID) : T {
    return this.findByIdOrNull(id) ?: throwNoSuchElement()
}

private fun throwNoSuchElement(): Nothing {
    throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
}