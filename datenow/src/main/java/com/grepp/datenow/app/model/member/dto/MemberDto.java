package com.grepp.datenow.app.model.member.dto;

import com.grepp.datenow.app.model.auth.code.Role;
import com.grepp.datenow.app.model.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class MemberDto {
  private String userId;
  private String password;
  private String email;
  private String name;
  private String nickname;
  private String phone;
  private String birth;
  private Role role;
  private LocalDateTime leavedAt;

  public static MemberDto from(Member member) {
    MemberDto dto = new MemberDto();
    dto.setUserId(member.getUserId());
    dto.setPassword(member.getPassword());
    dto.setEmail(member.getEmail());
    dto.setName(member.getName());
    dto.setNickname(member.getNickname());
    dto.setPhone(member.getPhone());
    dto.setBirth(member.getBirth());
    dto.setRole(member.getRole());
    dto.setLeavedAt(member.getLeavedAt());
    return dto;
  }
}
