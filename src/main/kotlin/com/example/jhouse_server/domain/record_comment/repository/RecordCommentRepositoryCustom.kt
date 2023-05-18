package com.example.jhouse_server.domain.record_comment.repository

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_comment.dto.RecordCommentResDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface RecordCommentRepositoryCustom {

    fun findNextRef(record: Record): Int

    fun findRecordComments(record: Record, pageable: Pageable): Page<RecordCommentResDto>
}