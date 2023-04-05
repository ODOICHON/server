package com.example.jhouse_server.admin.anaylsis.service

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AnalysisService (
        var userRepository: UserRepository
        ){

    fun getAnalysisAgeResult() : List<Double> {
        return userRepository.getAnalysisAgeResult()
    }
    fun getAnalysisJoinPathResult() : List<AnalysisJoinPathResponse> {
        return userRepository.getAnalysisJoinPathResults()
    }
}