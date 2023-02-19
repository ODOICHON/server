package com.example.jhouse_server.global.response

import com.example.jhouse_server.global.exception.ErrorCode
import com.fasterxml.jackson.annotation.JsonInclude
import javax.annotation.Generated

data class ApplicationResponse<T>(
        val code: String,
        val message: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val data : T?
) {
        @Generated
        companion object {
                fun ok() = ApplicationResponse(
                        code = "SUCCESS",
                        message = "성공",
                        data = null
                )

                fun <T> ok(data: T) = ApplicationResponse(
                        code = "SUCCESS",
                        message = "성공",
                        data = data
                )

                fun error(errorCode: ErrorCode) = ApplicationResponse<ErrorCode>(
                        code = errorCode.code,
                        message = errorCode.message,
                        data = null
                )

                fun error(errorCode: ErrorCode, message: String) = ApplicationResponse<ErrorCode>(
                        code = errorCode.code,
                        message = message,
                        data = null
                )
        }
}


