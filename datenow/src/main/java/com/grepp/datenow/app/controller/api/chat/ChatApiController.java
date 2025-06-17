package com.grepp.datenow.app.controller.api.chat;


import com.grepp.datenow.app.model.auth.domain.Principal;
import com.grepp.datenow.app.model.chat.dto.ChatDto;
import com.grepp.datenow.app.model.chat.dto.ChatRequestDto;
import com.grepp.datenow.app.model.chat.dto.ChattingResponseDto;
import com.grepp.datenow.app.model.chat.dto.ResponseChatRoomDto;
import com.grepp.datenow.app.model.chat.sevice.ChatService;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import com.grepp.datenow.infra.chat.config.RedisPublisher;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatApiController {

  //pubsub결국 발행자는 몰라도 되고 구독자들만 이제 내용을 받는다 느슨한 결합
  private final RedisPublisher redisPublisher;
  private final MemberRepository memberRepository;
  private final ChatService chatService;
//app/chat/send
  @MessageMapping("/chat/send")
  public ResponseEntity<?> sendMessage(ChatRequestDto dto,
      Authentication auth){


    Member user = memberRepository.findByUserId(auth.getName())
        .orElseThrow();
    int userNum = user.getId();

    ChatDto chatDto = new ChatDto(dto.getRoomId(),userNum,dto.getContent());

    chatService.sendChatMessage(chatDto);

    return ResponseEntity.ok("성공적으로 메세지가 보내졌습니다");


  }

  @GetMapping("/api/chatList")
  public ResponseEntity<?> chatRoomList(Authentication auth){
    Member user = memberRepository.findByUserId(auth.getName())
        .orElseThrow();
    log.info(auth.getName());
    List<ResponseChatRoomDto> dto = chatService.chatRoomList(user);

    return ResponseEntity.ok(dto);
  }

  @GetMapping("/api/chatList/{roomId}")
  public ResponseEntity<?> chatRoom(Authentication auth, @PathVariable Long roomId){
    log.info(auth.getName());
    List<ChattingResponseDto> chatting = chatService.userChatting(roomId, auth);

    return ResponseEntity.ok(chatting);

  }

  //랜덤 채팅방생성
  @PostMapping("/api/randomChat")
  public ResponseEntity<?> RandomNewChat(Authentication auth){

     Member user = memberRepository.findByUserId(auth.getName())
         .orElseThrow(() -> new EntityNotFoundException("해당 엔티티가 존재하지 않습니다"));

     Long roomId = chatService.RandomChatting(user);

    if (roomId == null) {
      return ResponseEntity.ok().body("");  // 빈 문자열 리턴
    }

     return ResponseEntity.ok(roomId);
  }

  @GetMapping("/api/userId")
  public ResponseEntity<?> getCurrentUserId(Authentication auth) {
    Member user = memberRepository.findByUserId(auth.getName())
        .orElseThrow(() -> new EntityNotFoundException("유저를 찾을 수 없습니다."));

    return ResponseEntity.ok(Map.of("userId", user.getId()));
  }


}
