package com.example.jhouse_server.domain.user

import com.example.jhouse_server.domain.comment.Comment
import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class User(
        var nickName: String,

        var phoneNum : String,

        @OneToMany(mappedBy = "user")
        val comments : MutableList<Comment> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val posts : MutableList<Post> = mutableListOf(),
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id : Long = 0L,
): BaseEntity() {
    fun update(phoneNum: String) {
        this.phoneNum = phoneNum
    }
}