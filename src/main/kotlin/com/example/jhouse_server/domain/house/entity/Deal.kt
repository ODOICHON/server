package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import java.util.Date
import javax.persistence.*

@Entity
class Deal(

    var dealDate: Date,

    var score : Int,

    var review: String?,

    @OneToOne
    var buyer: User,

    @OneToOne
    var house: House,

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {
}