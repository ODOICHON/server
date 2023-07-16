package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import java.util.stream.Collectors
import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class WithdrawalReason(val value: String) {

    ROW_USE("이용빈도 낮음"),
    RE_JOIN("재가입"),
    INSUFFICIENT_CONTENT("콘텐츠 및 정보 부족"),
    PERSONAL_PROTECTION("개인 정보 보호"),
    ETC("기타");

    companion object {
        fun getReasonByValue(value: String): WithdrawalReason {
            for(reason in WithdrawalReason.values()) {
                if(reason.value == value) {
                    return reason
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }

        fun getReasonByName(name: String): WithdrawalReason {
            for(reason in WithdrawalReason.values()) {
                if(reason.name == name) {
                    return reason
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}

@Converter(autoApply = true)
class WithdrawalReasonConverter: AttributeConverter<List<WithdrawalReason>, String> {
    override fun convertToDatabaseColumn(attribute: List<WithdrawalReason>): String {
        val list: List<String> = attribute.map { it.name }.toList()
        return list.stream().collect(Collectors.joining(","))
    }

    override fun convertToEntityAttribute(dbData: String): List<WithdrawalReason> {
        val list: List<String> = dbData.split(",")
        return list.map { WithdrawalReason.getReasonByName(it) }.toList()
    }
}