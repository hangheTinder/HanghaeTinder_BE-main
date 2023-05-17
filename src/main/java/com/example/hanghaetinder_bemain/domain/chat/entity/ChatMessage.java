package com.example.hanghaetinder_bemain.domain.chat.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.example.hanghaetinder_bemain.domain.common.entity.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class ChatMessage extends Timestamped implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private ChatRoom chatRoom;

    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK, ROOM
    }

    private String userId;
    private MessageType type; // 메시지 타입
    private String roomId; // 방번호
    private String sender; // 메시지 보낸사람
    private String message; // 메// 시지
    private Date createdAt;

    public ChatMessage(MessageType type, String roomId, String sender, String message, Date createdAt, ChatRoom chatRoom) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
        this.createdAt = createdAt;
        this.chatRoom = chatRoom;
    }

}
