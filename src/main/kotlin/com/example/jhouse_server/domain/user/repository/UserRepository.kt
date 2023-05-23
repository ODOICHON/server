package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.AdminType
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

    @Query("select u from User u where u.adminType = :adminType and u.id <> :userId")
    fun findAllByAdminType(@Param("userId") userId: Long, @Param("adminType") adminType: AdminType): List<User>
}