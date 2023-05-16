package com.example.jhouse_server.domain.record.service

import com.example.jhouse_server.domain.record.dto.*
import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.user.entity.User
import org.springframework.data.domain.Pageable
import javax.servlet.http.HttpServletRequest

interface RecordService {

    fun saveRecord(recordReqDto: RecordReqDto, user: User): Long

    fun updateRecord(recordUpdateDto: RecordUpdateDto, user: User, recordId: Long): Long

    fun deleteRecord(user: User, recordId: Long)

    fun getHotRecords(): RecordHotResDto

    fun getRecords(condition: RecordPageCondition, pageable: Pageable): RecordPageResDto

    fun getRecord(recordId: Long, request: HttpServletRequest): RecordResDto

    fun getRecordWithReview(recordId: Long): RecordWithReviewResDto

    fun getRevieweeRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): RecordPageResDto

    fun getReviewerRecords(condition: RecordReviewCondition, user: User, pageable: Pageable): RecordPageResDto

    fun updateRecordStatus(record: Record)
}