package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.UserJoinPath
import org.springframework.data.jpa.repository.JpaRepository

interface UserJoinPathRepository: JpaRepository<UserJoinPath, Long> {
}