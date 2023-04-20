package com.example.jhouse_server.global.exception

import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/v1/errors")
class ErrorCodeController {

    @GetMapping
    fun getErrorCodes() : ApplicationResponse<ErrorCodeView> {
        val errorCodes = Arrays.stream(ErrorCode.values())
            .collect(Collectors.toMap(ErrorCode::code, ErrorCode::message))
        return ApplicationResponse.ok(ErrorCodeView(errorCodes))
    }
}