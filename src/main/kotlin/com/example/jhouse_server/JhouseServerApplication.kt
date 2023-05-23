package com.example.jhouse_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling


@EnableScheduling
@SpringBootApplication
class JhouseServerApplication

fun main(args: Array<String>) {
    runApplication<JhouseServerApplication>(*args)
}
