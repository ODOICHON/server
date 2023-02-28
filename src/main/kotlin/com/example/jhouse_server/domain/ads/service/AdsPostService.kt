package com.example.jhouse_server.domain.ads.service

import com.example.jhouse_server.domain.ads.dto.AdsPostCreateReqDto
import com.example.jhouse_server.domain.ads.dto.AdsPostListResDto
import com.example.jhouse_server.domain.ads.dto.AdsPostResDto
import com.example.jhouse_server.domain.ads.dto.AdsPostUpdateReqDto
import com.example.jhouse_server.domain.post.dto.CodeResDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface AdsPostService {
    fun getPostAll() : List<AdsPostResDto>
    fun getPostOne(postId : Long) : AdsPostResDto
    fun updatePost(postId: Long, req : AdsPostUpdateReqDto, user : User) : Long
    fun createPost(req: AdsPostCreateReqDto, user: User) : Long
    fun deletePost(postId : Long, user: User)
    fun getPostAllByKeywordCustom(keyword: String, pageable: Pageable) : Page<AdsPostListResDto>
    fun fixPost(postId : Long, user: User) : Long
    fun getPostCategory(): List<CodeResDto>
}