package com.example.jhouse_server.global.exception


import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException::class)
    fun applicationException(ex: ApplicationException) : ResponseEntity<ApplicationResponse<ErrorCode>>{
        return ResponseEntity.status(ex.errorCode.status).body(ApplicationResponse.error(ex.errorCode))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun validationException(ex: MethodArgumentNotValidException) : ApplicationResponse<ErrorCode> {
        return ApplicationResponse.error(ErrorCode.INVALID_VALUE_EXCEPTION, ex.bindingResult.allErrors.get(0).defaultMessage!!)
    }

    @ExceptionHandler(RuntimeException::class)
    fun runtimeException(ex: RuntimeException) : ResponseEntity<ApplicationResponse<ErrorCode>> {
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_EXCEPTION.status)
                .body(ApplicationResponse.error(ErrorCode.INTERNAL_SERVER_EXCEPTION))
    }
}