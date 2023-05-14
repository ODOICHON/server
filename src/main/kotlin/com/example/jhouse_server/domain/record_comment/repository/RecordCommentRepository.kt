package com.example.jhouse_server.domain.record_comment.repository

import com.example.jhouse_server.domain.record_comment.entity.RecordComment
import org.springframework.data.jpa.repository.JpaRepository

interface RecordCommentRepository: JpaRepository<RecordComment, Long> {
}