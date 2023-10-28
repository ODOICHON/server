package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.comment.dto.CommentReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.user.entity.User

interface CommentService {

    fun getCommentAll(postId : Long) : List<CommentResDto>

    fun createComment(req: CommentReqDto, user: User) : Long

    fun updateComment(commentId: Long, req: CommentReqDto, user: User) : Long
    fun deleteComment(commentId: Long, user: User)
}