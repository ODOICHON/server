package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.UserTerm
import org.springframework.data.jpa.repository.JpaRepository

interface UserTermRepository : JpaRepository<UserTerm, Long>{
}