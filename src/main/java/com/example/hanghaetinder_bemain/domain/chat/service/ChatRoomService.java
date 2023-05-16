package com.example.hanghaetinder_bemain.domain.chat.service;

import org.springframework.stereotype.Service;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.member.entity.MatchMember;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	public ChatRoom createChatRoom(String name, MatchMember matchMember) {
		ChatRoom chatRoom = ChatRoom.create(name);

		chatRoomRepository.save(chatRoom);
		return chatRoom;
	}
}
