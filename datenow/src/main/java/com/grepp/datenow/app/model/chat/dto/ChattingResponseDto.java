package com.grepp.datenow.app.model.chat.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Getter

public class ChattingResponseDto {

  boolean usertrue;
  String nickname;
  String content;
  LocalDateTime dateTime;

}
