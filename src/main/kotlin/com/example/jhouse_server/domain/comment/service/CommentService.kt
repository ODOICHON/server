package com.example.jhouse_server.domain.comment.service

import com.example.jhouse_server.domain.comment.dto.CommentCreateReqDto
import com.example.jhouse_server.domain.comment.dto.CommentResDto
import com.example.jhouse_server.domain.comment.dto.CommentUpdateReqDto

interface CommentService {

    fun getCommentAll(postId : Long) : List<CommentResDto>

    fun createComment(req : CommentCreateReqDto) : CommentResDto

    fun updateComment(commentId: Long, req : CommentUpdateReqDto) : CommentResDto
}