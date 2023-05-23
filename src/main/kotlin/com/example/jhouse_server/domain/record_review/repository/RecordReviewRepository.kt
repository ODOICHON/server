package com.example.jhouse_server.domain.record_review.repository

import com.example.jhouse_server.domain.record_review.entity.RecordReview
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface RecordReviewRepository: JpaRepository<RecordReview, Long> {

    @EntityGraph(attributePaths = ["reviewer", "apply", "record"])
    @Query("select rr from RecordReview rr where rr.id = :reviewId")
    fun findWithUserAndRecordAndApply(@Param("reviewId") reviewId: Long): Optional<RecordReview>

    @EntityGraph(attributePaths = ["reviewer", "apply"])
    @Query("select rr from RecordReview rr where rr.id = :reviewId")
    fun findWithUserAndApply(@Param("reviewId") reviewId: Long): Optional<RecordReview>

    @EntityGraph(attributePaths = ["reviewer"])
    @Query("select rr from RecordReview rr where rr.record.id = :recordId order by rr.createdAt")
    fun findByRecordWithUser(@Param("recordId") recordId: Long): List<RecordReview>
}