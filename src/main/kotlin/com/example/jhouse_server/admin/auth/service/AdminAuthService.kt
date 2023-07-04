package com.example.jhouse_server.admin.auth.service

import com.example.jhouse_server.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AdminAuthService (
        var userRepository: UserRepository
        ){

}