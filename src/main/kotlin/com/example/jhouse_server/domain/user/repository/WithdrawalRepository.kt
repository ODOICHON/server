package com.example.jhouse_server.domain.user.repository

import com.example.jhouse_server.domain.user.entity.Withdrawal
import org.springframework.data.jpa.repository.JpaRepository

interface WithdrawalRepository: JpaRepository<Withdrawal, Long> {
}