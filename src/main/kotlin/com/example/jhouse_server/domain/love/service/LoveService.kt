package com.example.jhouse_server.domain.love.service

import com.example.jhouse_server.domain.user.entity.User

interface LoveService {
    fun loveBoard(postId: Long, user: User): Long
    fun hateBoard(postId: Long, user: User)
}