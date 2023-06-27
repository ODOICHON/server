package com.example.jhouse_server.domain.user.service.agent

import com.example.jhouse_server.domain.user.AgentSignUpReqDto
import com.example.jhouse_server.domain.user.entity.*
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.entity.agent.Estate
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.common.UserServiceCommonMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AgentServiceImpl(
    val userRepository: UserRepository,
    val userServiceCommonMethod: UserServiceCommonMethod
): AgentService {

    @Transactional
    override fun signUp(agentSignUpReqDto: AgentSignUpReqDto) {
        userServiceCommonMethod.validateDuplicate(agentSignUpReqDto.email, agentSignUpReqDto.nickName, agentSignUpReqDto.companyPhoneNum)

        val age: Age = Age.getAge(agentSignUpReqDto.age)!!
        val joinPaths: MutableList<JoinPath> = mutableListOf()
        for(joinPath in agentSignUpReqDto.joinPaths) {
            joinPaths.add(JoinPath.getJoinPath(joinPath)!!)
        }
        val address = agentSignUpReqDto.companyAddress.plus(" ").plus(agentSignUpReqDto.companyAddressDetail)

        val agent = Agent(agentSignUpReqDto.email, userServiceCommonMethod.encodePassword(agentSignUpReqDto.password),
            agentSignUpReqDto.nickName, agentSignUpReqDto.phoneNum, Authority.USER, age, UserType.AGENT,
            agentSignUpReqDto.agentCode, agentSignUpReqDto.businessCode, agentSignUpReqDto.companyName,
            agentSignUpReqDto.agentName, agentSignUpReqDto.companyPhoneNum, agentSignUpReqDto.assistantName,
            address, agentSignUpReqDto.companyEmail, Estate.getEstate(agentSignUpReqDto.estate), AgentStatus.WAIT)
        userRepository.save(agent)

        for(joinPath in joinPaths) {
            userServiceCommonMethod.saveUserJoinPath(joinPath, agent)
        }
    }

    @Transactional
    override fun approveStatus(user: User) {
        val agent = user as Agent
        agent.updateStatus(AgentStatus.APPROVE)
    }
}