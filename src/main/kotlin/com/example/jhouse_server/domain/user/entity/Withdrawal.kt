package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class Withdrawal(

    @Convert(converter = WithdrawalReasonConverter::class)
    val reason: List<WithdrawalReason>,

    @Column(length = 1000)
    val content: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {

}