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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final RedisPublisher redisPublisher;
  private final MemberRepository memberRepository;
  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final RedisTemplate redisTemplate;
  private final SimpMessageSendingOperations messagingTemplate;


  //메세지 보내고 redis에 담는 핵심 로직
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

  //채팅방 리스트 보여주는 로직
  public List<ResponseChatRoomDto> chatRoomList(Member user) {

    List<ChatRoom> chatRooms = chatRoomRepository.findAllByUser1OrUser2(user, user);


    return chatRooms.stream()
        .filter(room ->
            (room.getUser1().getId().equals(user.getId()) && room.isVisibleToUser1()) ||
                (room.getUser2().getId().equals(user.getId()) && room.isVisibleToUser2())
        )
        .map(room -> {
          String redisKey = "chat:lastMessage:" + room.getRoomId();
          String lastMessage = (String) redisTemplate.opsForValue().get(redisKey);

          // 상대방 닉네임 표시
          String otherNickname = room.getUser1().getId().equals(user.getId())
              ? room.getUser2().getNickname()
              : room.getUser1().getNickname();

          return ResponseChatRoomDto.builder()
              .roomId(room.getRoomId())
              .nickname(otherNickname)
              .lastMessage(lastMessage != null ? lastMessage : "채팅을 시작해보세요.")
              .build();
        })
        .toList();


  }

  //전에 했던 메세지 히스토리 보여주는 로직
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

  //랜덤채팅 시작 누르면 매칭 시작하는 로직
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
        .visibleToUser1(true)
        .visibleToUser2(true)
        .build();

    chatRoomRepository.save(chatRoom);

    //redis에 해당 유저의 채팅방을 지정을 해준다
    redisTemplate.opsForValue().set("chat" + user.getUserId() , chatRoom.getRoomId());
    redisTemplate.opsForValue().set("chat" + member1.getUserId() , chatRoom.getRoomId());

    // 매칭 결과 전달 구독자들에게
    messagingTemplate.convertAndSend("/topic/match." + user.getId(), Map.of("roomId", chatRoom.getRoomId()));
    messagingTemplate.convertAndSend("/topic/match." + member1.getId(), Map.of("roomId", chatRoom.getRoomId()));

    return chatRoom.getRoomId();
  }

  //채팅방 삭제 하는 로직
  @Transactional
  public void deleteByRoomId(Long roomId,Member member) {

    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));


    if (chatRoom.getUser1().getId().equals(member.getId())) {
      chatRoom.setVisibleToUser1(false);
    } else if (chatRoom.getUser2().getId().equals(member.getId())) {
      chatRoom.setVisibleToUser2(false);
    } else {
      throw new IllegalArgumentException("이 유저는 이 채팅방에 참여하지 않았습니다.");
    }

    chatRoomRepository.save(chatRoom);

  }

}
