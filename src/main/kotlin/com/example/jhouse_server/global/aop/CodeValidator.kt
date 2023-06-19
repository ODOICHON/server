package com.example.jhouse_server.global.aop

import com.example.jhouse_server.domain.board.service.getContent
import com.example.jhouse_server.domain.house.dto.HouseReqDto
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.springframework.stereotype.Component
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.Errors
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator

@Component
class CodeValidator: Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return HouseReqDto::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val houseReqDto: HouseReqDto = target as HouseReqDto
        if(getContent(houseReqDto.code).length > 10000) errors.rejectValue("code", ErrorCode.LENGTH_OUT_OF_CONTENTS.code, ErrorCode.LENGTH_OUT_OF_CONTENTS.message)
    }
}

@Component
@Aspect
class ValidatorAspect(private val codeValidator: CodeValidator) {

    @Pointcut("@annotation(EnableValidation)")
    fun enableValidation() {}

    @Before("enableValidation()")
    fun beforeValidation() {}

    @AfterReturning(value = "enableValidation()", returning = "result")
    fun afterValidation(result: Any?) {
        if (result is HouseReqDto) {
            val errors = BeanPropertyBindingResult(result, "houseReqDto")
            ValidationUtils.invokeValidator(codeValidator, result, errors)

            if (errors.hasErrors()) {
                throw ApplicationException(ErrorCode.INVALID_VALUE_EXCEPTION)
            }
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
annotation class EnableValidation