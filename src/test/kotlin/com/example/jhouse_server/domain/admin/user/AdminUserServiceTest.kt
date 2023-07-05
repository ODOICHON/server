package com.example.jhouse_server.domain.admin.user

import com.example.jhouse_server.admin.user.dto.join.AdminJoinAgentList
import com.example.jhouse_server.admin.user.dto.withdrawal.AdminWithdrawalList
import com.example.jhouse_server.admin.user.service.AdminUserService
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.domain.user.service.agent.AgentService
import com.example.jhouse_server.domain.user.service.agent.AgentServiceImpl
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class AdminUserServiceTest @Autowired constructor(
        var adminUserService: AdminUserService,
        var userService: UserService,
        var agentService: AgentService,
        var userRepository: UserRepository
){

    private val agentSignUpDto1 = MockEntity.testAgentSignUpDto()
    private val agentSignUpDto2 = MockEntity.testAgentSignUpDto2()
    private val agentSignUpDto3 = MockEntity.testAgentSignUpDto3()

    @BeforeEach
    fun beforeEach() {
        agentService.signUp(agentSignUpDto1)
        agentService.signUp(agentSignUpDto2)
        agentService.signUp(agentSignUpDto3)
    }

    private fun getAdminWithdrawalList(ids: List<Long>): AdminWithdrawalList {
        return AdminWithdrawalList(ids)
    }

    private fun getAdminJoinAgentList(ids: List<Long>): AdminJoinAgentList {
        return AdminJoinAgentList(ids)
    }


    @Test
    @DisplayName("관리자 - 공인중개사 or 일반회원 탈퇴")
    fun withdrawalAgentTest() {
        // given
        val agent1 = userRepository.findByEmail(agentSignUpDto1.email).get()
        val agent2 = userRepository.findByEmail(agentSignUpDto2.email).get()
        val agent3 = userRepository.findByEmail(agentSignUpDto3.email).get()

        userService.withdrawal(agent1)
        userService.withdrawal(agent2)
        userService.withdrawal(agent3)

        // when
        adminUserService.withdrawalUser(getAdminWithdrawalList(listOf(agent1.id, agent2.id, agent3.id)))

        // then
        assertThat(agent1.withdrawalStatus).isEqualTo(APPROVE)
        assertThat(agent2.withdrawalStatus).isEqualTo(APPROVE)
        assertThat(agent3.withdrawalStatus).isEqualTo(APPROVE)
    }

    @Test
    @DisplayName("공인중개사 가입승인")
    fun joinAgentTest() {
        // given
        val agent1 = userRepository.findByEmail(agentSignUpDto1.email).get() as Agent
        val agent2 = userRepository.findByEmail(agentSignUpDto2.email).get() as Agent
        val agent3 = userRepository.findByEmail(agentSignUpDto3.email).get() as Agent

        // when
        adminUserService.agentJoin(getAdminJoinAgentList(listOf(agent1.id, agent2.id, agent3.id)))

        // then
        assertThat(agent1.status).isEqualTo(AgentStatus.APPROVE)
        assertThat(agent2.status).isEqualTo(AgentStatus.APPROVE)
        assertThat(agent3.status).isEqualTo(AgentStatus.APPROVE)
    }



}