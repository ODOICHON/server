package com.example.jhouse_server.domain.record.entity.odori

import com.example.jhouse_server.domain.record.entity.Part
import com.example.jhouse_server.domain.record.entity.Record
import com.example.jhouse_server.domain.record.entity.RecordStatus
import com.example.jhouse_server.domain.user.entity.User
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
@DiscriminatorValue("O")
class Odori(
    category: OdoriCategory,
    record: Record
): Record(record.title!!, record.content!!, record.part!!, record.user!!) {

    @Enumerated(EnumType.STRING)
    var category: OdoriCategory = category
}