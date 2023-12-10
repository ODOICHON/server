package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.domain.user.entity.UserType
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository: JpaRepository<User, Long> , UserRepositoryCustom{

    @Query("select u from User u where u.id in (:ids)")
    fun findByIds(@Param("ids") ids: List<Long>): List<User>

    fun countByWithdrawalStatusAndUserType(status: WithdrawalStatus, userType: UserType): Long

    fun existsByUserName(userName: String) : Boolean

    fun existsByNickName(nickName: String): Boolean

    fun existsByPhoneNum(phoneNum: String): Boolean

    fun findByUserName(userName: String) : Optional<User>

    fun findByUserNameAndSuspension(userName : String, suspension: Boolean) : Optional<User>

    fun findByUserNameAndAuthority(userName: String, authority: Authority) : Optional<User>

    @Query("select u from User u where u.userType = :userType and u.id <> :userId")
    fun findAllByUserType(@Param("userId") userId: Long, @Param("userType") userType: UserType): List<User>
    fun findByNickName(nickName: String): Optional<User>

    fun findByEmail(email: String) : Optional<User>
}