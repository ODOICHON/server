package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class HouseTag(
    @Enumerated(EnumType.STRING)
    var recommendedTag: RecommendedTag,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "house_id")
    var house: House,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L,
) : BaseEntity() {
}