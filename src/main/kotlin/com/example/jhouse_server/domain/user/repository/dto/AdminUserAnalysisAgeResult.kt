package com.example.jhouse_server.domain.user.repository.dto

import com.example.jhouse_server.domain.user.entity.Age
import com.querydsl.core.annotations.QueryProjection
/**
 * ============================================================================================
 * AdminUserAnalysisAgeResult     -- 사용자 연령대 쿼리 결과 VO
 * age                            -- 연령대 Age enum class
 * count                          -- 개수
 * ============================================================================================
 * */
data class AdminUserAnalysisAgeResult @QueryProjection constructor(
        val age : Age,
        val count : Long
)
