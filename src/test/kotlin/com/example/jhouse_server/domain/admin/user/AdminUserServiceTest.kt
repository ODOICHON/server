package com.example.jhouse_server.domain.admin.user

import com.example.jhouse_server.admin.user.dto.withdrawal.AdminWithdrawalList
import com.example.jhouse_server.admin.user.service.AdminUserService
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus
import com.example.jhouse_server.domain.user.entity.WithdrawalStatus.*
import com.example.jhouse_server.domain.user.repository.UserRepository
import com.example.jhouse_server.domain.user.service.UserService
import com.example.jhouse_server.global.util.MockEntity
import org.assertj.core.api.Assertions.*
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
        var userRepository: UserRepository
){

    private val userSignUpDto = MockEntity.testUserSignUpDto()

    private fun getAdminWithdrawalList(id: Long): AdminWithdrawalList {
        return AdminWithdrawalList(listOf(id))
    }

    @Test
    @DisplayName("관리자 - 공인중개사 or 일반회원 탈퇴")
    fun withdrawalAgent() {
        // given
        userService.signUp(userSignUpDto)
        val user = userRepository.findByEmail(userSignUpDto.email).get()
        userService.withdrawal(user)

        // when
        adminUserService.withdrawalUser(getAdminWithdrawalList(user.id))

        // then
        assertThat(user.withdrawalStatus).isEqualTo(APPROVE)
    }



}