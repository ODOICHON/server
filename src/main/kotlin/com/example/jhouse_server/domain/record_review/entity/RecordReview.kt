package com.example.jhouse_server.domain.record_review.entity

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_review.dto.RecordReviewUpdateDto
import com.example.jhouse_server.domain.record_review_apply.entity.RecordReviewApply
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class RecordReview(

    var content: String,

    @Enumerated(EnumType.STRING)
    var status: RecordReviewStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id", nullable = false)
    var apply: RecordReviewApply,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    var record: Record,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var reviewer: User,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {

    fun updateRecordReview(recordReviewUpdateDto: RecordReviewUpdateDto) {
        this.content = recordReviewUpdateDto.content
        this.status = RecordReviewStatus.getStatus(recordReviewUpdateDto.status)
    }
}