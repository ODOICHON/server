package com.example.jhouse_server.domain.post.service

import com.example.jhouse_server.domain.post.dto.PostCreateReqDto
import com.example.jhouse_server.domain.post.dto.PostResDto
import com.example.jhouse_server.domain.post.dto.PostUpdateReqDto

interface PostService{

    fun getPostAll(): List<PostResDto>

    fun getPostOne(postId: Long) : PostResDto

    fun updatePost(postId: Long, req : PostUpdateReqDto) : PostResDto

    fun createPost(req : PostCreateReqDto) : PostResDto

}