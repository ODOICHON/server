package com.example.jhouse_server.global.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class AesUtil(
        @Value("\${aes.secret.key}") val key: String,
        @Value("\${aes.secret.alg}") val alg: String
) {
    private val iv: String = key.substring(0, 16)

    fun encrypt(text: String): String {
        val cipher: Cipher = Cipher.getInstance(alg)
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivParamSpec = IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParamSpec)

        val encrypted: ByteArray = cipher.doFinal(text.toByteArray(Charsets.UTF_8))
        return Base64.getEncoder().encodeToString(encrypted)
    }

    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance(alg)
        val keySpec = SecretKeySpec(key.toByteArray(), "AES")
        val ivParamSpec = IvParameterSpec(iv.toByteArray())
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParamSpec)

        val decodedBytes = Base64.getDecoder().decode(cipherText)
        val decrypted = cipher.doFinal(decodedBytes)
        return String(decrypted, Charsets.UTF_8)
    }
}