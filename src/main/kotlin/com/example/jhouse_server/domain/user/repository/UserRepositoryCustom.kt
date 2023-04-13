package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse


interface UserRepositoryCustom {
    fun getAnalysisAgeResult() : List<Double>

    fun getAnalysisJoinPathResults() : List<AnalysisJoinPathResponse>
}