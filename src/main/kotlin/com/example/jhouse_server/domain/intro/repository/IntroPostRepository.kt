package com.example.jhouse_server.domain.intro.repository

import com.example.jhouse_server.domain.intro.entity.IntroPost
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface IntroPostRepository : JpaRepository<IntroPost, Long> {
    @Query(nativeQuery = true,
        value = "select * from intro_post where strip_tags(intro_post.code) like concat('%', :keyword, '%')",
        countQuery = "select count(*) from intro_post")
    fun findAllByKeywordCustom(keyword : String, pageable: Pageable) : Page<IntroPost>
    fun findAllByIsSavedAndUseYn(isSaved : Boolean, useYn : Boolean, pageable: Pageable) : Page<IntroPost>
    fun findAllByIsSavedAndUseYnAndUser(isSaved : Boolean, useYn : Boolean, user : User, pageable: Pageable) : Page<IntroPost>
}