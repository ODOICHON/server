package com.example.jhouse_server.domain.user.controller.agent

import com.example.jhouse_server.domain.user.AgentSignUpReqDto
import com.example.jhouse_server.domain.user.service.agent.AgentService
import com.example.jhouse_server.global.response.ApplicationResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/agents")
class AgentController(
    val agentService: AgentService
) {

    @PostMapping("/sign-up")
    fun singUp(
        @Validated @RequestBody agentSignUpReqDto: AgentSignUpReqDto
    ): ApplicationResponse<Nothing> {
        agentService.signUp(agentSignUpReqDto)
        return ApplicationResponse.ok()
    }
}