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

    // TODO OneToOne 관계로 가져갈까 PK만 FK로 갖고 있을까
    var buyerId: Long?,

    @OneToOne
    var house: House,

    val fixed : Boolean = true, // 생성과 동시에 변경 불가능하도록 고정하기 위한 플래그 변수

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {
}