package com.example.hanghaetinder_bemain.controller.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import com.example.hanghaetinder_bemain.entity.ChatMessage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessageSendingOperations messagingTemplate;

	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		if (ChatMessage.MessageType.ENTER.equals(message.getType()))
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
	}
}