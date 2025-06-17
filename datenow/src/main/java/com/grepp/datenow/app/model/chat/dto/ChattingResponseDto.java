package com.grepp.datenow.app.model.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChattingResponseDto {

  boolean usertrue = true;
  String nickname;
  String content;
  LocalDateTime dateTime;

}
