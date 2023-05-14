package com.example.jhouse_server.domain.record_comment.entity

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class RecordComment(

    var content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    var record: Record,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {
}