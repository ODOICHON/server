package com.example.jhouse_server.domain.user.service

import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.Estate
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.agent.AgentService
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@Transactional
@SpringBootTest
class AgentServiceImplTest @Autowired constructor(
    val agentService: AgentService,
    val userRepository: UserRepository
) {

    private val agentSignUpDto = MockEntity.testAgentSignUpDto()

    @Test
    @DisplayName("공인중개사 테스트")
    fun signUpTest() {
        //given

        //when
        agentService.signUp(agentSignUpDto)

        //then
        val agent = userRepository.findByEmail(agentSignUpDto.email).get() as Agent
        assertThat(agent.email).isEqualTo(agentSignUpDto.email)
        assertThat(agent.nickName).isEqualTo(agentSignUpDto.nickName)
        assertThat(agent.phoneNum).isEqualTo(agentSignUpDto.phoneNum)
        assertThat(agent.agentCode).isEqualTo(agentSignUpDto.agentCode)
        assertThat(agent.businessCode).isEqualTo(agentSignUpDto.businessCode)
        assertThat(agent.companyName).isEqualTo(agentSignUpDto.companyName)
        assertThat(agent.agentName).isEqualTo(agentSignUpDto.agentName)
        assertThat(agent.companyPhoneNum).isEqualTo(agentSignUpDto.companyPhoneNum)
        assertThat(agent.companyEmail).isEqualTo(agentSignUpDto.companyEmail)
        assertThat(agent.estate).isEqualTo(Estate.APARTMENT)
        assertThat(agent.companyAddress).isEqualTo(agentSignUpDto.companyAddress + " " + agentSignUpDto.companyAddressDetail)
    }
}