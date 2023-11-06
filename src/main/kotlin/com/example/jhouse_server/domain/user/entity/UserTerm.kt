package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class UserTerm(
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    var term: Term,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L,
) :BaseEntity() {
}