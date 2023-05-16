package com.example.hanghaetinder_bemain.domain.chat.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;

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
	private long totalElements;
	private int totalPages;


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ChatMessageDto {
		private Long id;
		private ChatMessage.MessageType type;
		private String sender;
		private String message;
		private String createdAt;
	}

	public static ChatMessageListDto from(Page<ChatMessage> chatMessages) {
		List<ChatMessageDto> chatMessageDtos = chatMessages.getContent().stream()
			.map(chatMessage -> new ChatMessageDto(
				chatMessage.getId(),
				chatMessage.getType(),
				chatMessage.getSender(),
				chatMessage.getMessage(),
				chatMessage.getCreatedAt().toString()
			))
			.collect(Collectors.toList());

		ChatMessageListDto chatMessageListDto = new ChatMessageListDto();
		chatMessageListDto.setChatMessages(chatMessageDtos);
		chatMessageListDto.setTotalElements(chatMessages.getTotalElements());
		chatMessageListDto.setTotalPages(chatMessages.getTotalPages());

		return chatMessageListDto;

	}
}