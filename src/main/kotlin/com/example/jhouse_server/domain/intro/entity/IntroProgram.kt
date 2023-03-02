package com.example.jhouse_server.domain.intro.entity

import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import javax.persistence.*

@Entity
@Table(
    name = "intro_program"
)
class IntroProgram(
    var content : String, // 프로그램 내용
    var startDate : String, // 시작일자
    var endDate : String, // 종료일자
    var offeredTime : String, // 진행시간
    var capacity: Int, // 수용 인원
    @Convert(converter = UseCategoryConverter::class)
    var useYn : UseCategory, // 신청 가능 여부
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user : User,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {

    fun updateEntity(
        content : String,
        startDate: String,
        endDate: String,
        offeredTime: String,
        useYn: String,
        capacity: Int,
    ) : IntroProgram {
        this.capacity = capacity
        this.content = content
        this.startDate = startDate
        this.endDate = endDate
        this.offeredTime = offeredTime
        this.useYn = UseCategory.values().firstOrNull { it.name == useYn }
            ?: throw ApplicationException(ErrorCode.NOT_FOUND_EXCEPTION)
        return this
    }
}