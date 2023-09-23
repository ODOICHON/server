package com.example.jhouse_server.global.util

import com.example.jhouse_server.global.exception.ApplicationException
import com.example.jhouse_server.global.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMailMessage
import org.springframework.stereotype.Component
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

@Component
class EmailUtil(
    @Value("\${spring.mail.username}")
    private val username : String,
    private val javaMailSender: JavaMailSender,
) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun createMessage(email: String, code: String) : MimeMessage {
        log.info("보내는 대상: ", email)
        log.info("인증 번호 : " , code)
        val message = javaMailSender.createMimeMessage()
        message.addRecipients(MimeMessage.RecipientType.TO, email)
        message.subject = "[주말내집 인증번호] 주말내집 회원가입 인증번호"

        var msg = ""
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">이메일 주소 확인</h1>"
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 회원가입 화면에서 3분 이내에 입력해주세요.</p>"
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">"
        msg += code
        msg += "</td></tr></tbody></table></div>"

        message.setText(msg, "utf-8", "html")
        message.setFrom(InternetAddress(username, "주말내집 관리자"))

        return message
    }

    fun sendMessage(email: String, code: String) : String {
        log.debug("====== 이메일 메세지 생성 ======")
        val message = createMessage(email, code)
        try {
            javaMailSender.send(message)
        } catch (ex : MailException) {
            ex.printStackTrace()
            log.error("이메일 발송 실패")
            throw ApplicationException(ErrorCode.INTERNAL_SERVER_EXCEPTION)
        }
        return code
    }
}