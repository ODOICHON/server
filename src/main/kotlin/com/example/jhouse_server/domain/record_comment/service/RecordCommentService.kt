package com.example.jhouse_server.domain.record_comment.service

import com.example.jhouse_server.domain.record_comment.dto.RecordCommentReqDto
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentUpdateDto
import com.example.jhouse_server.domain.user.entity.User

interface RecordCommentService {

    fun saveRecordComment(recordCommentReqDto: RecordCommentReqDto, user: User): Long

    fun updateRecordComment(recordCommentUpdateDto: RecordCommentUpdateDto, user: User, commentId: Long): Long

    fun deleteRecordComment(user: User, commentId: Long)
}