package com.example.jhouse_server.domain.user.service.agent

import com.example.jhouse_server.domain.user.AgentSignUpReqDto
import com.example.jhouse_server.domain.user.entity.User

interface AgentService {
    fun signUp(agentSignUpReqDto: AgentSignUpReqDto)

    fun approveStatus(user: User)
}