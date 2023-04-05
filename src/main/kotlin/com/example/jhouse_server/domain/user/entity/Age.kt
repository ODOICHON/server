package com.example.jhouse_server.domain.user.entity

enum class Age (val value : String){
    TEN("20대 미만"),
    TWENTY("20대"),
    THIRTY("30대"),
    FORTY("40대"),
    FIFTY("50대"),
    SIXTY("60대 이상");


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