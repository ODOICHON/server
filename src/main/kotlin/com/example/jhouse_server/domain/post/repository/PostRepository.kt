package com.example.jhouse_server.domain.post.repository

import com.example.jhouse_server.domain.post.entity.Post
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Long> {
    @Query(nativeQuery = true,
    value = "select * from post where strip_tags(post.code) like concat('%', :keyword, '%')",
    countQuery = "select count(*) from post")
    fun findAllByKeywordCustom(keyword : String, pageable: Pageable) : Page<Post>
    fun findAllByIsSavedAndUseYn(isSaved : Boolean, useYn : Boolean, pageable: Pageable) : Page<Post>
    fun findAllByIsSavedAndUseYnAndUser(isSaved : Boolean, useYn : Boolean, user : User, pageable: Pageable) : Page<Post>

}