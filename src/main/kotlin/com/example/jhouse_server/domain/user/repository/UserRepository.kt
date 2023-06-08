package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository: JpaRepository<User, Long> , UserRepositoryCustom{

    fun existsByEmail(email: String): Boolean

    fun existsByNickName(nickName: String): Boolean

    fun existsByPhoneNum(phoneNum: String): Boolean

    fun findByEmail(email: String): Optional<User>

    fun findByEmailAndAuthority(email: String, authority: Authority) : Optional<User>

    @Query("select u from User u where u.userType = :userType and u.id <> :userId")
    fun findAllByUserType(@Param("userId") userId: Long, @Param("userType") userType: UserType): List<User>
}