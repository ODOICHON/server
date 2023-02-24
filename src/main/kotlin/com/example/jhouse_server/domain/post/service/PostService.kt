package com.example.jhouse_server.domain.post.service

import com.example.jhouse_server.domain.post.dto.PostCreateReqDto
import com.example.jhouse_server.domain.post.dto.PostResDto
import com.example.jhouse_server.domain.post.dto.PostUpdateReqDto
import com.example.jhouse_server.domain.user.entity.User

interface PostService{

    fun getPostAll(): List<PostResDto>

    fun getPostOne(postId: Long) : PostResDto

    fun updatePost(postId: Long, req: PostUpdateReqDto, user: User) : PostResDto

    fun createPost(req: PostCreateReqDto, user: User) : PostResDto
    fun deletePost(postId: Long, userId: User)

}