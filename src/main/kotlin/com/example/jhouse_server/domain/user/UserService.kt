package com.example.jhouse_server.domain.user

interface UserService {
    fun findUserById(userId : Long) : UserResDto
}