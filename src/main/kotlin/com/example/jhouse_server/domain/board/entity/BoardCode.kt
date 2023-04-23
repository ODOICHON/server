package com.example.jhouse_server.domain.board.entity

import com.example.jhouse_server.global.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(
    name = "board_code"
)
class BoardCode(
    @Column(length = Int.MAX_VALUE)
    var code: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long = 0L
) : BaseEntity() {

    fun updateEntity(
        code: String
    ) : BoardCode {
        this.code = code
        return this
    }
}