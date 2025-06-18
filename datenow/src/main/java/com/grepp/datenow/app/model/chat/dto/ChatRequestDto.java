package com.grepp.datenow.app.model.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRequestDto {
  Long roomId;
  String content;

}
