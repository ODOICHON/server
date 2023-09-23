package com.example.jhouse_server.domain.admin.user

import com.example.jhouse_server.admin.user.dto.AdminJoinAgentList
import com.example.jhouse_server.admin.user.dto.AdminWithdrawalList
import com.example.jhouse_server.admin.user.service.AdminUserService
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.domain.user.service.agent.AgentService
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

    private val userSignUpDto = MockEntity.testUserSignUpDto()
    private val agentSignUpDto1 = MockEntity.testAgentSignUpDto()
    private val agentSignUpDto2 = MockEntity.testAgentSignUpDto2()
    private val agentSignUpDto3 = MockEntity.testAgentSignUpDto3()

    @BeforeEach
    fun beforeEach() {
        agentService.signUp(agentSignUpDto1)
        agentService.signUp(agentSignUpDto2)
        agentService.signUp(agentSignUpDto3)
    }

    private fun getAdminWithdrawalList(id: Long): AdminWithdrawalList {
        return AdminWithdrawalList(listOf(id))
    }

    private fun getAdminJoinAgentList(ids: List<Long>): AdminJoinAgentList {
        return AdminJoinAgentList(ids)
    }

    @Test
    @DisplayName("관리자 - 공인중개사 or 일반회원 탈퇴")
    fun withdrawalAgent() {
        // given
        userService.signUp(userSignUpDto)
        val user = userRepository.findByUserNameAndSuspension(userSignUpDto.userName, false).get()
        val withdrawalUserReqDto = MockEntity.withdrawalUserReqDto(null)
        userService.withdrawal(user, withdrawalUserReqDto)

        // when
        adminUserService.withdrawalUser(getAdminWithdrawalList(user.id))

        // then
        assertThat(user.withdrawalStatus).isEqualTo(APPROVE)
    }

    @Test
    @DisplayName("공인중개사 가입승인")
    fun joinAgentTest() {
        // given
        val agent1 = userRepository.findByUserNameAndSuspension(agentSignUpDto1.userName, false).get() as Agent
        val agent2 = userRepository.findByUserNameAndSuspension(agentSignUpDto2.userName, false).get() as Agent
        val agent3 = userRepository.findByUserNameAndSuspension(agentSignUpDto3.userName, false).get() as Agent

        // when
        adminUserService.agentJoin(getAdminJoinAgentList(listOf(agent1.id, agent2.id, agent3.id)))

        // then
        assertThat(agent1.status).isEqualTo(AgentStatus.APPROVE)
        assertThat(agent2.status).isEqualTo(AgentStatus.APPROVE)
        assertThat(agent3.status).isEqualTo(AgentStatus.APPROVE)
    }



}