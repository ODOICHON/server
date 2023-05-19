package com.example.jhouse_server.domain.record_comment.repository

import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record_comment.entity.RecordComment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface RecordCommentRepository: JpaRepository<RecordComment, Long>, RecordCommentRepositoryCustom {

    @Modifying(clearAutomatically = true)
    @Query("update RecordComment rc set rc.step = rc.step + 1 where rc.step >= :step and rc.record = :record and rc.ref = :ref")
    fun updateStep(@Param("step") step: Long, @Param("record") record: Record, @Param("ref") ref: Long)
}