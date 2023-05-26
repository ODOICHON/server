package com.example.jhouse_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling

@EnableCaching
@EnableScheduling
@SpringBootApplication
class JhouseServerApplication

fun main(args: Array<String>) {
    runApplication<JhouseServerApplication>(*args)
}