package com.example.jhouse_server.admin.user.service

import com.example.jhouse_server.admin.user.dto.LoginForm
import com.example.jhouse_server.domain.user.entity.Authority
import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.validation.BindingResult

@Service
class AdminUserService (
        var userRepository: UserRepository
        ){

}