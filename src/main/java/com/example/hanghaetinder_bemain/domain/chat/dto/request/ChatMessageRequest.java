package com.example.hanghaetinder_bemain.domain.chat.dto.request;

import lombok.Getter;

@Getter
public class ChatMessageRequest {
	private Long chatRoomId;
	private Long senderId;
	private String content;

	// getter, setter, constructor 생략
}