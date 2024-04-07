package com.example.jhouse_server.domain.house.entity

import com.example.jhouse_server.domain.user.entity.User
import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class Report(

    @JoinColumn(name = "house_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var house: House,

    @JoinColumn(name = "owner_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var owner: User,

    @JoinColumn(name = "reporter_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var reporter: User,

    @Convert(converter = ReportTypeConverter::class)
    var reportType: ReportType,

    @Column(nullable = true, length = 101)
    var reportReason: String,

    @Id
    @GeneratedValue
    @Column(name = "report_id")
    var id: Long = 0L

): BaseEntity() {
}