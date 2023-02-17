package com.example.jhouse_server.global

import org.jasypt.encryption.StringEncryptor
import org.jasypt.encryption.pbe.PooledPBEByteEncryptor
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JasyptConfig(
        @Value("\${jasypt.encryptor.password}")
        private val password: String
){
    @Bean("jasyptStringEncryptor")
    fun stringEncryptor(): StringEncryptor {
        val encryptor = PooledPBEStringEncryptor()
        val config = SimpleStringPBEConfig()
        config.password = password
        config.algorithm = "PBEWITHMD5AndDES"
        config.setKeyObtentionIterations("1000")
        config.setPoolSize("1")
        config.stringOutputType = "base64"
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator")
        encryptor.setConfig(config)
        return encryptor
    }
}