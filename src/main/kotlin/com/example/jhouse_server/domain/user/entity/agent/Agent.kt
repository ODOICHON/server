package com.example.jhouse_server.domain.user.entity.agent

import com.example.jhouse_server.domain.user.entity.*
import javax.persistence.*

@Entity
@DiscriminatorValue("A")
class Agent(
    email: String, password: String, nickName: String,
    phoneNum: String, authority: Authority, age: Age, userType: UserType,

    @Convert(converter = CryptoConverter::class)
    var agentCode: String,

    @Convert(converter = CryptoConverter::class)
    var businessCode: String,

    var companyName: String,

    @Convert(converter = CryptoConverter::class)
    var agentName: String,

    @Convert(converter = CryptoConverter::class)
    var companyPhoneNum: String,

    var assistantName: String?,

    @Convert(converter = CryptoConverter::class)
    var companyAddress: String,

    @Convert(converter = CryptoConverter::class)
    var companyEmail: String,

    @Enumerated(EnumType.STRING)
    var estate: Estate,

    @Enumerated(EnumType.STRING)
    var status: AgentStatus
): User(email, password, nickName, phoneNum, authority, age, userType, withdrawalStatus = null) {

    fun updateStatus(status: AgentStatus) {
        this.status = status
    }
}