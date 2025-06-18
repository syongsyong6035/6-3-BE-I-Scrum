package com.grepp.datenow.app.model.chat.dto;

import com.grepp.datenow.app.model.chat.code.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseChatRoomDto {

  private Long roomId;
  private String nickname;
  private String lastMessage;
}
