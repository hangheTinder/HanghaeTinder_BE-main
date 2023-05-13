package com.example.hanghaetinder_bemain.controller.chat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.hanghaetinder_bemain.dto.resoponse.chat.ChatMessageListDto;
import com.example.hanghaetinder_bemain.entity.ChatMessage;
import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.repository.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;

	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		if (ChatMessage.MessageType.ENTER.equals(message.getType()))
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		chatMessageRepository.save(message);

		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
	}

	@GetMapping("/room/{roomId}/messages")
	public ResponseEntity<ChatMessageListDto> roomMessages(@PathVariable Long roomId) {

		Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomId);
		if (chatRoomOptional.isPresent()) {
			List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(chatRoomOptional.get().getRoomId());
			ChatMessageListDto chatMessageListDto = ChatMessageListDto.from(chatMessages);
			return ResponseEntity.ok().body(chatMessageListDto);
		}
		return ResponseEntity.notFound().build();
	}
}