package com.example.jhouse_server.domain.record.entity.retrospection

import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.user.entity.User
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
@DiscriminatorValue("R")
class Retrospection(
    category: RetrospectionCategory,
    title: String,
    content: String,
    part: Part,
    status: RecordStatus,
    user: User
): Record(title, content, part, status, user) {

    @Enumerated(EnumType.STRING)
    var category: RetrospectionCategory = category
}