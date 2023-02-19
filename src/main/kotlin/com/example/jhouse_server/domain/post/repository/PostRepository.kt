package com.example.jhouse_server.domain.post.repository

import com.example.jhouse_server.domain.post.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
}