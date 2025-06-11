package com.grepp.datenow.app.model.member.dto;

import com.grepp.datenow.app.model.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminSearchUserDto {
  String nickName;
  String userId;
  String name;
  LocalDateTime created_at;

  public AdminSearchUserDto(Member member){
    this.nickName = member.getNickname();
    this.userId = member.getUserId();
    this.name = member.getName();
    this.created_at = member.getCreatedAt();
  }

}
