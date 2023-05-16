package com.example.hanghaetinder_bemain.domain.chat.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAll();

    @Query("SELECT CR FROM ChatRoom CR WHERE CR.id = :id")
    ChatRoom findRoomId(@Param("id") Long id);

    Optional<ChatRoom> findById(Long id);

    @Query("SELECT cr FROM ChatRoom cr LEFT JOIN fetch cr.messages m WHERE cr.id = :id ORDER BY m.createdAt DESC")
    List<ChatRoom> findAllById(@Param("id") Long id);


}
