package com.grepp.datenow.app.controller.web.member.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateRequest {

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
    private String phone;

    @Email
    private String email;

    private String nickname;
}