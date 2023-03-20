package com.example.jhouse_server.global.util

import org.hibernate.boot.MetadataBuilder
import org.hibernate.boot.spi.MetadataBuilderContributor
import org.hibernate.dialect.function.StandardSQLFunction
import org.hibernate.type.StandardBasicTypes

class SqlFunctionsMetadataBuilderContributor: MetadataBuilderContributor {
    override fun contribute(metadataBuilder: MetadataBuilder) {
        metadataBuilder.applySqlFunction("strip_tags",
        StandardSQLFunction("strip_tags", StandardBasicTypes.STRING))
    }
}