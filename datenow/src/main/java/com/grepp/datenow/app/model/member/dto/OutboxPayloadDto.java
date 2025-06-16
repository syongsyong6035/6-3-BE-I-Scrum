package com.grepp.datenow.app.model.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// outbox payload 에 들어갈 정보를 DTO 로 맨들어버려
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxPayloadDto {
    private String email;
    private String verifyToken;
    private String domain;
}
