package com.example.jhouse_server.domain.user

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserServiceImpl (
        val userRepository: UserRepository
        ): UserService {

    override fun findUserById(userId: Long): UserResDto {
        val findUser = userRepository.findById(userId)
                .orElseThrow{ IllegalStateException("회원을 찾을 수 없습니다.")}
        return toDto(findUser)
    }

    @Transactional
    override fun createUser(req: UserReqDto): UserResDto {
        return userRepository.save(User(req.nickName, req.phoneNum)).run { toDto(this) }
    }
}