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
    val analysisService: AnalysisService
        ){

    @GetMapping("/join-path")
    fun getAnalysisJoinPath(model : Model) : String {
        val result = analysisService.getAnalysisJoinPathResult()
        model.addAttribute("joinPathResults", result)
        return "analysis/joinPath"

    }

    @GetMapping("/age")
    fun getAnalysisAge(model : Model) : String {
        val result = analysisService.getAnalysisAgeResult()
        val label = Age.values().map { it.value }
        model.addAttribute("rate", result.map { it.rate })
        model.addAttribute("count", result.map { it.count })
        model.addAttribute("label", label)
        return "analysis/age"
    }


}