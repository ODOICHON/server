package com.example.jhouse_server.domain.record.entity

import com.example.jhouse_server.domain.record.dto.RecordUpdateDto
import com.example.jhouse_server.domain.record_comment.entity.RecordComment
import com.example.jhouse_server.domain.record_review.entity.RecordReview
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
class Record(): BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L

    var title: String? = null

    var content: String? = null

    var hits: Int = 0

    @Enumerated(EnumType.STRING)
    var part: Part? = null

    @Enumerated(EnumType.STRING)
    var status: RecordStatus = RecordStatus.WAIT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null

    @OneToMany(mappedBy = "record", cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var reviews: MutableList<RecordReview> = mutableListOf()

    @OneToMany(mappedBy = "record", cascade = [CascadeType.PERSIST], orphanRemoval = true)
    var comments: MutableList<RecordComment> = mutableListOf()

    constructor(title: String, content: String, part: Part, user: User): this() {
        this.title = title
        this.content = content
        this.part = part
        this.user = user
    }

    fun updateRecord(recordUpdateDto: RecordUpdateDto) {
        this.title = recordUpdateDto.title
        this.content = recordUpdateDto.content
    }

    fun updateHits() {
        this.hits++
    }
}