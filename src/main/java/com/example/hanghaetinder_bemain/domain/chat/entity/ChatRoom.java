package com.example.hanghaetinder_bemain.domain.chat.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import com.example.hanghaetinder_bemain.domain.common.entity.Timestamped;
import com.example.hanghaetinder_bemain.domain.member.entity.MatchMember;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ChatRoom extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomId;
    private String name;

    @OneToOne(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private MatchMember matchMember;

    @OneToMany(mappedBy = "chatRoom")
    @OrderBy("createdAt DESC")
    private List<ChatMessage> messages;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();;
        chatRoom.name = name;
        return chatRoom;
    }
}
