package com.example.jhouse_server.domain.record_review_apply.repository

import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApply
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional

interface RecordReviewApplyRepository: JpaRepository<RecordReviewApply, Long> {

    @EntityGraph(attributePaths = ["record"])
    @Query("select rra from RecordReviewApply rra where rra.record.id = :recordId and rra.reviewer.id = :userId")
    fun findWithRecord(@Param("recordId") recordId: Long, @Param("userId") userId: Long): Optional<RecordReviewApply>

    @EntityGraph(attributePaths = ["reviewer"])
    @Query("select rra from RecordReviewApply rra where rra.record.id = :recordId order by rra.id")
    fun findByRecordWithUser(@Param("recordId") recordId: Long): List<RecordReviewApply>
}