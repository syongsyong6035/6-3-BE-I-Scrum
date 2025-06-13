package com.grepp.spring.infra.event

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.grepp.spring.app.model.MailService
import com.grepp.spring.app.model.code.MailTemplatePath
import com.grepp.spring.infra.mail.MailTemplate
import com.grepp.spring.infra.mail.SmtpDto
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


interface MessageDelegate {
    fun handleMessage(message: String)
}

@Component
class EventMessageDelegate(
    private val mailTemplate: MailTemplate,
    private val objectMapper: ObjectMapper
) : MessageDelegate {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun handleMessage(message: String) = runBlocking {
        log.info(message)
        val outbox = objectMapper.readValue(message, OutboxDto::class.java)
        if(outbox.eventType.uppercase() == MailTemplatePath.SIGNUP_VERIFY.name){
//            sendSignupCompleteMail(outbox)
            sendSignupVerifyMail(outbox)
        }
    }

    private suspend fun sendSignupCompleteMail(outbox: OutboxDto) {
        val dto = SmtpDto(
            from = "grepp",
            to = outbox.payload,
            subject = "환영합니다! 고객님",
            templatePath = MailTemplatePath.SIGNUP_COMPLETE.path
        )
        mailTemplate.send(dto)
    }

    // 새로운 회원가입 인증 메일 발송 로직
    private suspend fun sendSignupVerifyMail(outbox: OutboxDto) {
        try {
            // Outbox.payload (JSON 문자열)를 OutboxPayloadDto로 역직렬화
            val payloadDto = objectMapper.readValue(outbox.payload, OutboxPayloadDto::class.java)

            val dto = SmtpDto(
                from = "DateNow", // 발신자 설정 (환경 변수 등으로 관리하는 것이 좋습니다)
                to = payloadDto.email, // payloadDto에서 이메일 추출
                subject = "회원가입을 완료해주세요: 이메일 인증", // 제목 설정
                templatePath = MailTemplatePath.SIGNUP_VERIFY.path, // 실제 템플릿 경로로 변경 (예: resources/templates/mail/signup-verify.html)
                properties = mapOf( // 템플릿에서 사용할 변수들
                    "token" to payloadDto.verifyToken,
                    "domain" to payloadDto.domain // 인증 링크 생성을 위한 도메인
                )
            )
            mailTemplate.send(dto)
            log.info("Sent signup verification mail to: {}", payloadDto.email)
        } catch (e: JsonProcessingException) {
            log.error("Failed to parse Outbox payload JSON for signup_verify event: {}", outbox.payload, e)
        } catch (e: Exception) {
            log.error("Error sending signup verification mail for payload: {}", outbox.payload, e)
        }
    }
}