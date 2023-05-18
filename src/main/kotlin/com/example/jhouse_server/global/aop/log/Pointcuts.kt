package com.example.jhouse_server.global.aop.log

import org.aspectj.lang.annotation.Pointcut

class Pointcuts {

    @Pointcut("execution(* com.example.jhouse_server.domain..*.*(..)) || execution(* com.example.jhouse_server.admin..*.*(..))")
    fun all(){}


    @Pointcut("execution(* com.example.jhouse_server.domain..*Service.*(..)) || execution(* com.example.jhouse_server.admin..*Service.*(..))")
    fun allService(){}

    @Pointcut("execution(* com.example.jhouse_server.domain..*Repository.*(..))")
    fun allQuery(){}
}