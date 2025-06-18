package com.grepp.datenow.app.model.chat.entity;

import com.grepp.datenow.app.model.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long messageNum;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id")
  private Member id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "roomId")
  private ChatRoom roomId;

  private String content;


  private LocalDateTime dateTime;

  public ChatMessage(Member id, ChatRoom roomId, String content,
      LocalDateTime dateTime) {

    this.id = id;
    this.roomId = roomId;
    this.content = content;
    this.dateTime = dateTime;
  }
}
