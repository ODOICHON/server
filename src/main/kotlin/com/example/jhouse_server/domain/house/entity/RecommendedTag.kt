package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import java.util.stream.Collectors
import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class RecommendedTag(val value : String) {
    WANT_TO_INTERIOR_FOR_THE_FIRST_TIME("처음부터 인테리어 하고 싶어요."),
    WANT_TO_READY_HOUSE("어느 정도 준비된 집이 좋아요."),
    HAVE_CHILDREN("아이와 함께 살아요."),
    WANT_TO_LOOK_A_GOOD_VIEW("경치가 좋은 집을 원해요."),
    WANT_TO_FARM("농사 짓기를 원해요.")
    ;


    companion object {
        fun getValueByTagName(name: String) : String {
            for(tag in RecommendedTag.values()) {
                if(tag.name == name) {
                    return tag.value
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }

        fun getTagByName(name: String) : RecommendedTag {
            for(tag in RecommendedTag.values()) {
                if(tag.name == name) {
                    return tag
                }
            }
            throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        }
    }
}

