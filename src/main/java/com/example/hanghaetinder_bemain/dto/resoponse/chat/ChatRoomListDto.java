package com.example.hanghaetinder_bemain.dto.resoponse.chat;

import java.util.List;
import java.util.stream.Collectors;

import com.example.hanghaetinder_bemain.entity.ChatMessage;
import com.example.hanghaetinder_bemain.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListDto {

	private List<ChatRoomDto> chatRooms;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ChatRoomDto {
		private Long id;
		private String name;
		private String roomId;
	}
	public static ChatRoomListDto from(List<ChatRoom> chatRooms) {
		List<ChatRoomDto> chatRoomDtos = chatRooms.stream()
			.map(chatRoom -> new ChatRoomListDto.ChatRoomDto(
				chatRoom.getId(),
				chatRoom.getName(),
				chatRoom.getRoomId()))
			.collect(Collectors.toList());

		return new ChatRoomListDto(chatRoomDtos);
	}

}
