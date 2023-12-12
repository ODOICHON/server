package com.example.jhouse_server.domain.house.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Address(
        @Column(length = 100)
        var city: String, // 시도

        @Column(length = 100)
        var detail : String?, // 상세주소

        @Column(length = 6)
        var zipCode: String, // 우편번호
) {
    fun updateEntity(city: String, detail: String?, zipCode: String) : Address {
        this.city = city
        this.detail = detail
        this.zipCode = zipCode
        return this
    }
}