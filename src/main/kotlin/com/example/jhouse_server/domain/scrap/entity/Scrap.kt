package com.example.jhouse_server.domain.scrap.entity

import com.example.jhouse_server.domain.house.entity.House
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class Scrap(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var subscriber: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    var house : House,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) : BaseEntity() {
}