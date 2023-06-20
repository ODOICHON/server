package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class Deal(

    @Convert(converter = DealStateConverter::class)
    var dealState : DealState,

    @OneToOne
    var buyer: User,

    @OneToOne
    var house: House,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {
}