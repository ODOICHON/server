package com.example.jhouse_server.domain.post.service

import com.example.jhouse_server.domain.post.dto.*
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface PostService{

    fun getPostAll(): List<PostResDto>

    fun getPostOne(postId: Long) : PostResDto

    fun updatePost(postId: Long, req: PostUpdateReqDto, user: User) : Long

    fun createPost(req: PostCreateReqDto, user: User) : Long
    fun deletePost(postId: Long, user: User)
    fun getPostAllByKeywordCustom(keyword: String, pageable: Pageable): Page<PostListResDto>
    fun getPostCategory(): List<CodeResDto>

}