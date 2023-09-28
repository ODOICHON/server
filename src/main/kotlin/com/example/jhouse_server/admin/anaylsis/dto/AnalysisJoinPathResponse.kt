package com.example.jhouse_server.admin.anaylsis.dto

data class AnalysisJoinPathResponse(
        val joinPath : String,
        val rate : Double,
        val count : Int,
)

data class AnalysisAgeResponse(
        val age: String,
        val rate : Double,
        val count: Int,
)