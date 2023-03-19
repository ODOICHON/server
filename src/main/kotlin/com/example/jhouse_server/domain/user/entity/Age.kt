package com.example.jhouse_server.domain.user.entity

enum class Age {
    TEN("20대 미만"),
    TWENTY("20대"),
    THIRTY("30대"),
    FORTY("40대"),
    FIFTY("50대"),
    SIXTY("60대 이상");

    private val value: String

    constructor(value: String) {
        this.value = value
    }

    companion object {
        fun getAge(value: String): Age? {
            for (age in Age.values()) {
                if (age.value == value) {
                    return age
                }
            }
            return null
        }
    }
}