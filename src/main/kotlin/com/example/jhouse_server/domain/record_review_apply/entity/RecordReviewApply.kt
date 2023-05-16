package com.example.jhouse_server.domain.record_review_apply.entity

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_review.entity.RecordReview
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class RecordReviewApply(

    @Enumerated(EnumType.STRING)
    var status: RecordReviewApplyStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    var record: Record,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var reviewer: User,

    @OneToMany(mappedBy = "apply", cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var reviews: MutableList<RecordReview> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {

    fun updateStatus(status: RecordReviewApplyStatus) {
        this.status = status
    }
}