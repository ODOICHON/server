package com.example.jhouse_server.domain.comment.entity

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(
        name = "comment"
)
class Comment(

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
        var post : Post,

        var content : String,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var user : User,

        @Id @GeneratedValue
        val id : Long = 0L,
): BaseEntity() {
    fun updateEntity(content : String) : Comment {
        this.content = content
        return this
    }
}