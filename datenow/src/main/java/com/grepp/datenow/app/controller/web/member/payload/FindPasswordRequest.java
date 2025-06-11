package com.grepp.datenow.app.controller.web.member.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FindPasswordRequest {

    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효한 이메일 형식으로 입력하세요.")
    private String email;

}
