package com.grepp.datenow.app.model.chat.dto;

import com.grepp.datenow.app.model.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateChatRoomDto {

  Member user1;
  Member user2;
  LocalDateTime dateTime;

}
