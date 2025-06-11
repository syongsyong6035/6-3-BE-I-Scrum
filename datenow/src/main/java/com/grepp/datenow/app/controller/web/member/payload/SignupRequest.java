package com.grepp.datenow.app.controller.web.member.payload;

import com.grepp.datenow.app.model.member.dto.MemberDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    
    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$")
    private String userId;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    private String birth;

    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
    private String phone;

    public MemberDto toDto(){
        MemberDto memberDto = new MemberDto();
        memberDto.setUserId(this.userId);
        memberDto.setPassword(this.password);
        memberDto.setEmail(this.email);
        memberDto.setName(this.name);
        memberDto.setNickname(this.nickname);
        memberDto.setBirth(this.birth);
        memberDto.setPhone(this.phone);
        return memberDto;
    }
}
