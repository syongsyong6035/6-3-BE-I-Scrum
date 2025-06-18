package com.grepp.datenow.app.model.chat.dto;

import com.grepp.datenow.app.model.chat.entity.ChatRoom;
import com.grepp.datenow.app.model.member.entity.Member;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ResponseChatDto {
  private ChatRoom roomId;
  private Member senderId;
  private String content;
  private LocalDateTime dateTime;

}
