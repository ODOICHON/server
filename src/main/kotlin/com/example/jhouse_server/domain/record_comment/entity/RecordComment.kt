package com.example.jhouse_server.domain.record_comment.entity

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentUpdateDto
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

    var ref: Long,

    var step: Long,

    var level: Long,

    var allChildrenSize: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: RecordComment?,

    @OneToMany(mappedBy = "parent")
    var children: MutableList<RecordComment> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {

    fun updateRecordComment(recordCommentUpdateDto: RecordCommentUpdateDto) {
        this.content = recordCommentUpdateDto.content
    }

    fun updateAllChildrenSize() {
        this.allChildrenSize++
    }

    fun updateStep() {
        this.step++
    }
}