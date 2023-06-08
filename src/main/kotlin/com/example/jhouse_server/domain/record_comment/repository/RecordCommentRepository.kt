package com.example.jhouse_server.domain.record_comment.repository

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_comment.entity.RecordComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RecordCommentRepository: JpaRepository<RecordComment, Long>, RecordCommentRepositoryCustom {

    @Query("select rc from RecordComment rc where rc.record = :record and rc.ref = :ref")
    fun findSameRef(@Param("record") record: Record, @Param("ref") ref: Long): List<RecordComment>
}