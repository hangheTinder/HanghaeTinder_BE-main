package com.example.hanghaetinder_bemain.domain.chat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	Slice<ChatMessage> findByRoomIdOrderByCreatedAtAsc(String roomId);

	Page<ChatMessage> findByRoomId(String roomId, Pageable pageable);

	@Query("SELECT m FROM ChatMessage m WHERE m.roomId = :roomId ORDER BY m.createdAt DESC")
	List<ChatMessage> findLatestMessageByRoomId(@Param("roomId") String roomId, Pageable pageable);
}



