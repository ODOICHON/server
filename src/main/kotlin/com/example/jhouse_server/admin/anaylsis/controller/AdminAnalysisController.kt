package com.example.jhouse_server.admin.anaylsis.controller

import com.example.jhouse_server.admin.anaylsis.service.AnalysisService
import com.example.jhouse_server.domain.user.entity.Age
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/admin/analysis")
class AdminAnalysisController (
    /**
     * =============================================================================================
     *  DI for Service
     * =============================================================================================
     * */
    val analysisService: AnalysisService
        ){
    /**
     * =============================================================================================
     * 사용자(일반 사용자, 공인중개사 ) 회원 가입 경로 조회
     *
     * @author YoonTaeminnnn
     * @param model
     * @return analysis/joinPath 회원 가입 경로 페이지
     * =============================================================================================
     * */
    @GetMapping("/join-path")
    fun getAnalysisJoinPath(model : Model) : String {
        val result = analysisService.getAnalysisJoinPathResult()
        model.addAttribute("joinPathResults", result) // 가입 경로 결과
        return "analysis/joinPath"

    }
    /**
     * =============================================================================================
     * 사용자 연령대 조회
     *
     * @author YoonTaeminnnn
     * @param model
     * @return analysis/age 연령대 페이지
     * =============================================================================================
     * */
    @GetMapping("/age")
    fun getAnalysisAge(model : Model) : String {
        val result = analysisService.getAnalysisAgeResult()
        val label = Age.values().map { it.value } // Age enum class value 리스트 추출
        model.addAttribute("rate", result.map { it.rate })
        model.addAttribute("count", result.map { it.count })
        model.addAttribute("label", label)
        return "analysis/age"
    }


}