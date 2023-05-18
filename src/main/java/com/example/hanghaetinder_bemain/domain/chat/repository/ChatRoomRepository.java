package com.example.hanghaetinder_bemain.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;



@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAll();

    @Query("SELECT CR FROM ChatRoom CR WHERE CR.roomId = :roomId")
    ChatRoom findRoomId(@Param("roomId") String roomId);

    Optional<ChatRoom> findByRoomId(String RoomId);
    void deleteByRoomId(String yourRoomId);

}
