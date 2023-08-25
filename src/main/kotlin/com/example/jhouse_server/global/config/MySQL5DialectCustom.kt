package com.example.jhouse_server.global.config

import org.hibernate.dialect.MySQL57Dialect
import org.hibernate.dialect.function.SQLFunctionTemplate
import org.hibernate.type.StandardBasicTypes


class MySQL5DialectCustom : MySQL57Dialect() {
    init {
        registerFunction(
            "match",
            SQLFunctionTemplate(StandardBasicTypes.INTEGER, "match(?1) against (?2 in boolean mode)")
        )
    }
}