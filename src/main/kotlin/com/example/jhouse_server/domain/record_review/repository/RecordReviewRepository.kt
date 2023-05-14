package com.example.jhouse_server.domain.record_review.repository

import com.example.jhouse_server.domain.record_review.entity.RecordReview
import org.springframework.data.jpa.repository.JpaRepository

interface RecordReviewRepository: JpaRepository<RecordReview, Long> {
}