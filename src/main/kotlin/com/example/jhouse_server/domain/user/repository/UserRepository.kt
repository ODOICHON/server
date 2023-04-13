package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> , UserRepositoryCustom{

    fun existsByEmail(email: String): Boolean

    fun existsByNickName(nickName: String): Boolean

    fun existsByPhoneNum(phoneNum: String): Boolean

    fun findByEmail(email: String): Optional<User>

    fun findByEmailAndAuthority(email: String, authority: Authority) : Optional<User>

}