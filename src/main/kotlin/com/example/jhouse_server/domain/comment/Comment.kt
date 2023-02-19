package com.example.jhouse_server.domain.comment

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(
        name = "comment"
)
class Comment(
        id: Long = 0L,
        content: String,
        post: Post,
): BaseEntity(id) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post : Post = post
        protected set

    var content : String = content
        protected set
}