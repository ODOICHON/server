package com.example.jhouse_server.global.util

import net.nurigo.java_sdk.api.Message
import net.nurigo.java_sdk.exceptions.CoolsmsException
import org.json.simple.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SmsUtil (
        @Value("\${cool.sms.api-key}") val key: String,
        @Value("\${cool.sms.api-secret}") val secret: String,
        @Value("\${cool.sms.from}") val from: String
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun sendMessage(phoneNum: String, code: String) {
        val message = Message(key, secret)
        var params: HashMap<String, String> = HashMap()

        params["to"] = phoneNum
        params["from"] = from
        params["type"] = "SMS"
        params["text"] = "[주말내집 인증번호] 본인확인 인증번호($code)를 입력해주세요."

        try {
            val obj: JSONObject = message.send(params)
            log.info(obj.toString())
        } catch (e: CoolsmsException) {
            throw RuntimeException("문자 전송에 실패했습니다.")
        }
    }
}