package com.grepp.datenow.infra.chat.config;

import com.grepp.datenow.app.model.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

  private final RedisTemplate<String, Object> redisTemplate;
  private final PatternTopic topic;

  public void sendMessagePublish(ChatDto chatDto) {

    redisTemplate.convertAndSend("chat."+chatDto.getRoomId(), chatDto);



  }
}
