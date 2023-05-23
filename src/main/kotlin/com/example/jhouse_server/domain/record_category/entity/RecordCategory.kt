package com.example.jhouse_server.domain.record_category.entity

import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
class RecordCategory(
    var template: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", unique = true)
    var category: RecordCategoryEnum,

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long = 0L
): BaseEntity() {
}