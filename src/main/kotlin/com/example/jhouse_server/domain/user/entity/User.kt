package com.example.jhouse_server.domain.user.entity

import com.example.jhouse_server.domain.ads.entity.AdPost
import com.example.jhouse_server.domain.comment.entity.Comment
import com.example.jhouse_server.domain.intro.entity.IntroPost
import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class User(
        var email: String,

        var password: String,

        var nickName: String,

        var phoneNum : String,

        @Enumerated(EnumType.STRING) var authority: Authority,

        @OneToMany(mappedBy = "user")
        val comments : MutableList<Comment> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val posts : MutableList<Post> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val adPosts : MutableList<AdPost> = mutableListOf(),

        @OneToMany(mappedBy = "user")
        val introPosts : MutableList<IntroPost> = mutableListOf(),

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