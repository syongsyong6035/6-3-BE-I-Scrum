package com.grepp.datenow.app.model.chat.repository;

import com.grepp.datenow.app.model.chat.entity.ChatRoom;
import com.grepp.datenow.app.model.member.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {


  List<ChatRoom> findAllByUser1OrUser2(Member user, Member user1);

  void deleteByRoomId(Long roomId);
}
