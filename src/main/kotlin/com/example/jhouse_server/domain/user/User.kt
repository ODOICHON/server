package com.example.jhouse_server.domain.user

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
        var nickName: String,
        var phoneNum : String,
) {
    @Id @GeneratedValue val id: Long? = null

    fun update(phoneNum: String) {
        this.phoneNum = phoneNum
    }
}