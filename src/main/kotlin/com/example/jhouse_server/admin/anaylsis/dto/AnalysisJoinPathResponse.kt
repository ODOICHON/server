package com.example.jhouse_server.admin.anaylsis.dto

/**
 * =============================================================================================
 * AnalysisJoinPathResponse -- 회원 가입 경로 응답 DTO
 * joinPath                 -- 가입경로명
 * rate                     -- 비율
 * count                    -- 실제 수
 * =============================================================================================
 */
data class AnalysisJoinPathResponse(
        val joinPath : String,
        val rate : Double,
        val count : Int,
)
/**
 * =============================================================================================
 * AnalysisAgeResponse      -- 회원 연령대 응답 DTO
 * age                      -- 연령대명 ( Age enum class 참고 )
 * rate                     -- 비율
 * count                    -- 실제 수
 * =============================================================================================
 */
data class AnalysisAgeResponse(
        val age: String,
        val rate : Double,
        val count: Int,
)