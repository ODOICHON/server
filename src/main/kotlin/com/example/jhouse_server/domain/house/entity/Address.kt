package com.example.jhouse_server.domain.house.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Address(
        @Column
        var city: String, // 시도

        @Column
        var zipcode: String, // 우편번호
) {
    fun updateEntity(city: String, zipCode: String) : Address {
        this.city = city
        this.zipcode = zipCode
        return this
    }
}