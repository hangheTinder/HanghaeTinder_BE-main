package com.example.hanghaetinder_bemain.domain.chat.dto.response;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Getter
@Setter
@NoArgsConstructor
public class ChatRoomListDto {

	private List<ChatRoomDto> chatRooms;

	public ChatRoomListDto(List<ChatRoomDto> chatRooms) {
		this.chatRooms = chatRooms;
	}

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ChatRoomDto {
		private Long id;
		private String name;
		private String roomId;
		private Date lastMsgDate;
		private String lastMsg;
	}

}
