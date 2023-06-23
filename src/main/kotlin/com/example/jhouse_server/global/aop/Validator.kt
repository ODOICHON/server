package com.example.jhouse_server.global.aop

import com.example.jhouse_server.domain.board.service.getContent
import java.lang.annotation.Documented
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass


class CodeValidator : ConstraintValidator<CodeValid, String>{
    override fun isValid(value: String, context: ConstraintValidatorContext?): Boolean {
        return getContent(value).length > 10000
    }

}

@Target(*[AnnotationTarget.FIELD])
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = arrayOf(CodeValidator::class))
@Documented
annotation class CodeValid(
    val message: String = "게시글의 내용이 10,000자를 넘을 수 없습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)