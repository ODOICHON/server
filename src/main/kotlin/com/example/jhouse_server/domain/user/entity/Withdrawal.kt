package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Withdrawal(

    @Convert(converter = WithdrawalReasonConverter::class)
    val reason: List<WithdrawalReason>,

    val content: String?,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {

}