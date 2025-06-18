package com.grepp.datenow.app.model.chat.repository;

import com.grepp.datenow.app.model.chat.entity.ChatMessage;
import com.grepp.datenow.app.model.chat.entity.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  @Query("SELECT m FROM ChatMessage m JOIN FETCH m.id WHERE m.roomId = :chatRoom")
  List<ChatMessage> findAllByRoomIdOrderByDateTimeAsc(ChatRoom chatRoom);

  void deleteByRoomId(ChatRoom chatRoom);
}
