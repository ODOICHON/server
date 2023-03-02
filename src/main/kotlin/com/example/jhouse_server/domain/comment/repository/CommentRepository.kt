package com.example.jhouse_server.domain.comment.repository

import com.example.jhouse_server.domain.comment.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Long> {
}