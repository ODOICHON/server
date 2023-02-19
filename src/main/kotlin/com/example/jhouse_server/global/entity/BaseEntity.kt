package com.example.jhouse_server.global.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id : Long = 0L
) {

    @CreatedDate
    @Column(updatable = false)
    var createdAt : LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column(updatable = true)
    var updatedAt : LocalDateTime = LocalDateTime.now()

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as BaseEntity

        if(id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}