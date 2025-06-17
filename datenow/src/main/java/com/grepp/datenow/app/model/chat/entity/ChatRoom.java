package com.grepp.datenow.app.model.chat.entity;

import com.grepp.datenow.app.model.chat.code.MessageType;
import com.grepp.datenow.app.model.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roomId;

  @ManyToOne
  @JoinColumn(name = "user1")
  private Member user1;

  @ManyToOne
  @JoinColumn(name = "user2" )
  private Member user2;

//  @Enumerated(EnumType.STRING)
//  private MessageType type;


  private String lastMessage;



}
