package com.example.jhouse_server.domain.intro.service

import com.example.jhouse_server.domain.intro.dto.IntroPostCreateReqDto
import com.example.jhouse_server.domain.intro.dto.IntroPostListResDto
import com.example.jhouse_server.domain.intro.dto.IntroPostResDto
import com.example.jhouse_server.domain.intro.dto.IntroPostUpdateReqDto
import com.example.jhouse_server.domain.post.dto.CodeResDto
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface IntroPostService {
    fun getPostAll() : List<IntroPostResDto>
    fun getPostOne(postId : Long) : IntroPostResDto
    fun updatePost(postId : Long, req: IntroPostUpdateReqDto, user: User) : Long
    fun createPost(req: IntroPostCreateReqDto, user: User) : Long
    fun deletePost(postId: Long, user: User)
    fun getPostAllByKeywordCustom(keyword : String, pageable: Pageable) : Page<IntroPostListResDto>
    fun getPostCategory(): List<CodeResDto>
}