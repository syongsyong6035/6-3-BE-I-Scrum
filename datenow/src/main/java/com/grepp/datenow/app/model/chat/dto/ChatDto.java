package com.grepp.datenow.app.model.chat.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatDto {

  Long roomId;
  int senderId;
  String content;
  LocalDateTime dateTime;


  public ChatDto(Long roomId, int senderId, String content) {
    this.roomId = roomId;
    this.senderId = senderId;
    this.content = content;
    this.dateTime = LocalDateTime.now();
  }
}
