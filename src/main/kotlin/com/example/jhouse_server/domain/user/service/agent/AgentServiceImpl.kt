package com.example.jhouse_server.domain.user.service.agent

import com.example.jhouse_server.domain.user.AgentSignUpReqDto
import com.example.jhouse_server.domain.user.DefaultUser
import com.example.jhouse_server.domain.user.entity.*
import com.example.jhouse_server.domain.user.entity.agent.Agent
import com.example.jhouse_server.domain.user.entity.agent.AgentStatus
import com.example.jhouse_server.domain.user.entity.agent.Estate
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.common.UserServiceCommonMethod
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
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

        val terms: MutableList<Term> = mutableListOf()
        for(term in agentSignUpReqDto.terms) {
            terms.add(Term.getTerm(term)!!)
        }
        if(!terms.containsAll(listOf(Term.SERVICE_USED_AGREE, Term.PERSONAL_INFO_NOTI))) {
            throw ApplicationException(ErrorCode.DISAGREE_TERM)
        }

        val address = agentSignUpReqDto.companyAddress.plus(" ").plus(agentSignUpReqDto.companyAddressDetail)

        val agent = Agent(agentSignUpReqDto.email, userServiceCommonMethod.encodePassword(agentSignUpReqDto.password),
            agentSignUpReqDto.nickName, agentSignUpReqDto.phoneNum, DefaultUser().profileImageUrl, Authority.USER, age, UserType.AGENT,
            agentSignUpReqDto.agentCode, agentSignUpReqDto.businessCode, agentSignUpReqDto.companyName,
            agentSignUpReqDto.agentName, agentSignUpReqDto.companyPhoneNum, agentSignUpReqDto.assistantName,
            address, agentSignUpReqDto.companyEmail, Estate.getEstate(agentSignUpReqDto.estate), AgentStatus.WAIT)
        userRepository.save(agent)

        for(joinPath in joinPaths) {
            userServiceCommonMethod.saveUserJoinPath(joinPath, agent)
        }

        for(term in terms) {
            userServiceCommonMethod.saveUserTerm(term, agent)
        }
    }

    @Transactional
    override fun approveStatus(user: User) {
        val agent = user as Agent
        agent.updateStatus(AgentStatus.APPROVE)
    }
}