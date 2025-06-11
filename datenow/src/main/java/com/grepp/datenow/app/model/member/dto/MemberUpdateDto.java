package com.grepp.datenow.app.model.member.dto;

import lombok.Data;

@Data
public class MemberUpdateDto {
    private String password;
    private String phone;
    private String email;
    private String nickname;
}
