package com.example.jhouse_server.admin.anaylsis.service

import com.example.jhouse_server.admin.anaylsis.dto.AnalysisAgeResponse
import com.example.jhouse_server.admin.anaylsis.dto.AnalysisJoinPathResponse
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AnalysisService (
    /**
     * =============================================================================================
     *  DI for Repository
     * =============================================================================================
     * */
        val userRepository: UserRepository
        ){
    /**
     * =============================================================================================
     *  사용자 연령대 분석을 위한 데이터 조회
     *
     *  @return List<AnalysisAgeResponse>
     * =============================================================================================
     * */
    fun getAnalysisAgeResult() : List<AnalysisAgeResponse> {
        return userRepository.getAnalysisAgeResult()
    }
    /**
     * =============================================================================================
     *  사용자 회원 가입 경로 분석을 위한 데이터 조회
     *
     *  @return List<AnalysisJoinPathResponse>
     * =============================================================================================
     * */
    fun getAnalysisJoinPathResult() : List<AnalysisJoinPathResponse> {
        return userRepository.getAnalysisJoinPathResults()
    }
}