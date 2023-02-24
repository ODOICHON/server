package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.CommentUpdateReqDto
import com.example.jhouse_server.domain.user.entity.User

interface CommentService {

    fun getCommentAll(postId : Long) : List<CommentResDto>

    fun createComment(req: CommentCreateReqDto, user: User) : CommentResDto

    fun updateComment(commentId: Long, req: CommentUpdateReqDto, user: User) : CommentResDto
    fun deleteComment(commentId: Long, user: User)
}