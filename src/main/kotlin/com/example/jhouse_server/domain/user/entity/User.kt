package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.domain.board.entity.Board
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class User(
        @Convert(converter = CryptoConverter::class)
    var email: String,

        @Convert(converter = CryptoConverter::class)
    var password: String,

        @Convert(converter = CryptoConverter::class)
    var nickName: String,

        @Convert(converter = CryptoConverter::class)
    var phoneNum : String,

        @Convert(converter = CryptoConverter::class)
    @Enumerated(EnumType.STRING) var authority: Authority,

        @Convert(converter = CryptoConverter::class)
        @Enumerated(EnumType.STRING) var age: Age,

        @OneToMany(mappedBy = "user")
        val joinPaths: MutableList<UserJoinPath> = mutableListOf(),

    @OneToMany(mappedBy = "user")
        val comments : MutableList<Comment> = mutableListOf(),

    @OneToMany(mappedBy = "user")
        val boards : MutableList<Board> = mutableListOf(),

    @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id : Long = 0L,
): BaseEntity() {
    fun update(phoneNum: String) {
        this.phoneNum = phoneNum
    }

    fun updateNickName(nickName: String) {
        this.nickName = nickName
    }

    fun updatePassword(password: String) {
        this.password = password
    }
}