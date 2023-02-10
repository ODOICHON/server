package com.example.jhouse_server.domain

interface UserService {
    fun findUserById(userId : Long) : UserResDto
}