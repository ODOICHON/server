package com.example.jhouse_server.domain.ads.repository

import com.example.jhouse_server.domain.ads.entity.AdPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface AdsPostRepository : JpaRepository<AdPost, Long> {

    @Query(nativeQuery = true,
        value = "select * from ads_post where strip_tags(ads_post.code) like concat('%', :keyword, '%')",
        countQuery = "select count(*) from ads_post")
    fun findAllByKeywordCustom(keyword: String, pageable: Pageable) : Page<AdPost>
}