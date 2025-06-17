package com.grepp.datenow.infra.chat.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grepp.datenow.app.model.chat.dto.ChatDto;
import com.grepp.datenow.app.model.chat.entity.ChatMessage;
import com.grepp.datenow.app.model.chat.entity.ChatRoom;
import com.grepp.datenow.app.model.chat.repository.ChatRoomRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisSubsctiber implements MessageListener {

  private final ObjectMapper objectMapper;
  private final SimpMessageSendingOperations messageSendingOperations;

  @Override
  public void onMessage(Message message, byte[] pattern) {
    try {
      String chat = new String(message.getBody(), StandardCharsets.UTF_8);
      ChatDto dto = objectMapper.readValue(chat, ChatDto.class);

      log.info("redis subscriber received message: {}", dto.getContent());

      //이제 보낼때 값 보내야한다
      messageSendingOperations.convertAndSend("/topic/chat."+dto.getRoomId(),dto);


    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

  }


}
