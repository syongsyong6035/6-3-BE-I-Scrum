package com.grepp.datenow.app.model.chat.sevice;

import com.grepp.datenow.app.model.chat.dto.ChatDto;
import com.grepp.datenow.app.model.chat.dto.ChattingResponseDto;
import com.grepp.datenow.app.model.chat.dto.ResponseChatRoomDto;
import com.grepp.datenow.app.model.chat.entity.ChatMessage;
import com.grepp.datenow.app.model.chat.entity.ChatRoom;
import com.grepp.datenow.app.model.chat.repository.ChatMessageRepository;
import com.grepp.datenow.app.model.chat.repository.ChatRoomRepository;
import com.grepp.datenow.app.model.member.entity.Member;
import com.grepp.datenow.app.model.member.repository.MemberRepository;
import com.grepp.datenow.infra.chat.config.RedisPublisher;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final RedisPublisher redisPublisher;
  private final MemberRepository memberRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final RedisTemplate redisTemplate;
  private final SimpMessageSendingOperations messagingTemplate;


  public void sendChatMessage(ChatDto dto) {
    Member member = memberRepository.findById(dto.getSenderId())
        .orElseThrow();
    ChatRoom chatRoom = chatRoomRepository.findById(dto.getRoomId())
        .orElseThrow();

    //메세지를  db에 저장한다
    ChatMessage chatMessage = new ChatMessage(member, chatRoom, dto.getContent(), dto.getDateTime());
    chatMessageRepository.save(chatMessage);

    String redisKey = "chat:lastMessage:" + dto.getRoomId();
    redisTemplate.opsForValue().set(redisKey, dto.getContent());

    redisPublisher.sendMessagePublish(dto);
  }

  public List<ResponseChatRoomDto> chatRoomList(Member user) {

    List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser1OrUser2(user, user);


    return chatRooms.stream()
        .map(room -> {
          String redisKey = "chat:lastMessage:" + room.getRoomId();
          String lastMessage = (String) redisTemplate.opsForValue().get(redisKey);

          return ResponseChatRoomDto.builder()
              .roomId(room.getRoomId())
              .nickname(user.getNickname())
              .lastMessage(lastMessage != null ? lastMessage : "채팅을 시작해보세요.")
              .build();
        })
        .toList();


  }

  public List<ChattingResponseDto> userChatting(Long roomId, Authentication auth) {
    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow();

    List<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByDateTimeAsc(chatRoom);


    Member currentUser = memberRepository.findByUserId(auth.getName())
        .orElseThrow();

    return chatMessages.stream()
        .map(msg -> ChattingResponseDto.builder()
            .content(msg.getContent())
            .nickname(msg.getId().getNickname()) // 메시지 보낸 사람 닉네임
            .dateTime(msg.getDateTime())
            .usertrue(msg.getId().getId().equals(currentUser.getId())) // 내가 보낸 메시지인지 여부
            .build())
        .toList();



  }

  public Long RandomChatting(Member user) {

    String watingUserId = (String) redisTemplate.opsForList().leftPop("user_wating");

    if(watingUserId == null || watingUserId.equals(user.getUserId())){
      // 없을때는 나를 대기열에 넣는다
      redisTemplate.opsForList().rightPush("user_wating",user.getUserId());
      return null;

    }

    Member member1 = memberRepository.findByUserId(watingUserId)
        .orElseThrow(() -> new EntityNotFoundException("해당 멤버가 존재하지 않습니다"));
    ChatRoom chatRoom = ChatRoom.builder()
        .user1(member1)
        .user2(user)
        .lastMessage("")
        .build();

    chatRoomRepository.save(chatRoom);

    //redis에 해당 유저의 채팅방을 지정을 해준다
    redisTemplate.opsForValue().set("chat" + user.getUserId() , chatRoom.getRoomId());
    redisTemplate.opsForValue().set("chat" + member1.getUserId() , chatRoom.getRoomId());

    // 🔔 여기에서 양쪽 사용자에게 매칭 결과 전송
    messagingTemplate.convertAndSend("/topic/match." + user.getUserId(), Map.of("roomId", chatRoom.getRoomId()));
    messagingTemplate.convertAndSend("/topic/match." + member1.getUserId(), Map.of("roomId", chatRoom.getRoomId()));

    return chatRoom.getRoomId();
  }
}
