package com.example.hanghaetinder_bemain.dto.resoponse.chat;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.example.hanghaetinder_bemain.entity.ChatMessage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageListDto {

	private List<ChatMessageDto> chatMessages;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ChatMessageDto {
		private Long id;
		private ChatMessage.MessageType type;
		private String sender;
		private String message;
		private Date createdAt;
		private String lastMsg;
	}

	public static ChatMessageListDto from(List<ChatMessage> chatMessages) {
		List<ChatMessageDto> chatMessageDtos = chatMessages.stream()
			.map(chatMessage -> new ChatMessageDto(
				chatMessage.getId(),
				chatMessage.getType(),
				chatMessage.getSender(),
				chatMessage.getMessage(),
				chatMessage.getCreatedAt(),
				chatMessage.getChatRoom().getMessages().get(0).toString()
			))
			.collect(Collectors.toList());

		return new ChatMessageListDto(chatMessageDtos);
	}
}