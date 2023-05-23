package com.example.jhouse_server.global.schedule

import com.example.jhouse_server.domain.record.repository.RecordRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class Scheduler(
    private val recordRepository: RecordRepository
) {

    /**
     * 매일 0시 0분 0초 마다 등록된지 1주일이 지났는데도 WAIT 상태인 글들은 전부 삭제한다.
     */
    @Scheduled(cron = "0 0 0 * * *")
    fun schedulingRecord() {
        val records = recordRepository.findWeekAgo(LocalDateTime.now())
        recordRepository.deleteAll(records)
    }
}