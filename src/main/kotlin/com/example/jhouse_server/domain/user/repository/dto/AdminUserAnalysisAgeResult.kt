package com.example.jhouse_server.domain.user.repository.dto

import com.example.jhouse_server.domain.user.entity.Age
import com.querydsl.core.annotations.QueryProjection

data class AdminUserAnalysisAgeResult @QueryProjection constructor(
        val age : Age,
        val count : Long
)
