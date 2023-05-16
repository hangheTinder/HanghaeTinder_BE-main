package com.example.hanghaetinder_bemain.domain.chat.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.domain.chat.dto.response.ChatMessageListDto;
import com.example.hanghaetinder_bemain.domain.chat.dto.response.ChatRoomListDto;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.domain.member.service.MemberService;
import com.example.hanghaetinder_bemain.domain.chat.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ChatController {

	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatMessageService chatMessageService;
	private final ChatRoomRepository chatRoomRepository;
	private final MatchMemberRepository matchMemberRepository;
	private final MemberRepository memberRepository;
	private final MemberService memberService;

	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {
		chatMessageService.save(message);

		messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
	}

	@GetMapping("/api/room/{Rid}/messages")
	public ResponseEntity<ChatMessageListDto> roomMessages(@PathVariable String Rid) {

		Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomId(Rid);
		if (chatRoomOptional.isPresent()) {
			List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(chatRoomOptional.get().getRoomId());
			ChatMessageListDto chatMessageListDto = ChatMessageListDto.from(chatMessages);
			return ResponseEntity.ok().body(chatMessageListDto);
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	@GetMapping("/api/user/room")
	public ResponseEntity<ChatRoomListDto> chatRooms(@AuthenticationPrincipal final UserDetailsImpl userDetails) {

		Optional<Member> member = memberRepository.findById(userDetails.getId());
		List<ChatRoom> matchMemberOptional = matchMemberRepository.findMatchmember(member.get().getId());
		if (matchMemberOptional.size() != 0) {
			ChatRoomListDto chatRoomListDto = ChatRoomListDto.from(matchMemberOptional);
			return ResponseEntity.ok().body(chatRoomListDto);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/api/user/chat/{id}")
	public String roomDetail(@PathVariable Long id) {
		//websocket
		String RoomId = chatRoomRepository.findRoomId(id).getRoomId();
		return RoomId;
	}

	@GetMapping("/api/user-info")
	@ResponseBody
	public Member getUserName(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return userDetails.getMember();
	}
}