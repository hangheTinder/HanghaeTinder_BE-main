package com.example.hanghaetinder_bemain.domain.chat.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;

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
		private String sender;
		private String message;
		private String createdAt;
		public ChatMessageDto(String sender, String message, Date createdAt) {
			LocalDate localDate = new java.sql.Date(createdAt.getTime()).toLocalDate();
			this.sender = sender;
			this.message = message;
			this.createdAt = localDate.toString();
		}
	}


	public static ChatMessageListDto from(Page<ChatMessage> chatMessages) {
		List<ChatMessageDto> chatMessageDtos = chatMessages.getContent().stream()
			.map(chatMessage -> new ChatMessageDto(
				chatMessage.getId(),
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