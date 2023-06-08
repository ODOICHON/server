package com.example.jhouse_server.domain.record.entity.technology

import com.example.jhouse_server.domain.record.entity.Record
import javax.persistence.DiscriminatorValue
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
@DiscriminatorValue("T")
class Technology(
    category: TechnologyCategory,
    record: Record
): Record(record.title!!, record.content!!, record.part!!, record.user!!) {

    @Enumerated(EnumType.STRING)
    var category: TechnologyCategory = category
}