package com.example.hanghaetinder_bemain.controller.chat;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.hanghaetinder_bemain.dto.resoponse.chat.ChatMessageListDto;
import com.example.hanghaetinder_bemain.dto.resoponse.chat.ChatRoomListDto;
import com.example.hanghaetinder_bemain.entity.ChatMessage;
import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.entity.MatchMember;
import com.example.hanghaetinder_bemain.entity.Member;
import com.example.hanghaetinder_bemain.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.repository.MemberRepository;
import com.example.hanghaetinder_bemain.sevice.chat.ChatMessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ChatController {

	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatMessageService chatMessageService;
	private final ChatRoomRepository chatRoomRepository;
	private final MatchMemberRepository matchMemberRepository;
	private final MemberRepository memberRepository;

	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		if (ChatMessage.MessageType.ENTER.equals(message.getType()))
			message.setMessage(message.getSender() + "님이 입장하셨습니다.");
		chatMessageService.save(message);

		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
	}

	@GetMapping("/room/{Rid}/messages")
	public ResponseEntity<ChatMessageListDto> roomMessages(@PathVariable Long Rid) {

		Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(Rid);
		if (chatRoomOptional.isPresent()) {
			List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(chatRoomOptional.get().getRoomId());
			ChatMessageListDto chatMessageListDto = ChatMessageListDto.from(chatMessages);
			return ResponseEntity.ok().body(chatMessageListDto);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/room/user/{id}")
	public ResponseEntity<ChatRoomListDto> chatRooms(@PathVariable Long id) {

		Optional<Member> member = memberRepository.findById(id);
		Optional<MatchMember> matchMemberOptional = matchMemberRepository.findMatchmember(member.get().getId());
		if (matchMemberOptional.isPresent()) {
			List<ChatRoom> chatRoomList = chatRoomRepository.findAllById(matchMemberOptional.get().getChatRoom().getId());
			ChatRoomListDto chatRoomListDto = ChatRoomListDto.from(chatRoomList);
			return ResponseEntity.ok().body(chatRoomListDto);
		}
		return ResponseEntity.notFound().build();
	}

}