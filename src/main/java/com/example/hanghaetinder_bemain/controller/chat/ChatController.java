package com.example.hanghaetinder_bemain.controller.chat;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.dto.http.DefaultDataRes;
import com.example.hanghaetinder_bemain.dto.http.ResponseMessage;
import com.example.hanghaetinder_bemain.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.dto.resoponse.LoginResponseDto;
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
import com.example.hanghaetinder_bemain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.sevice.MemberService;
import com.example.hanghaetinder_bemain.sevice.chat.ChatMessageService;

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
	public ResponseEntity<ChatMessageListDto> roomMessages(@PathVariable Long Rid) {

		Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(Rid);
		if (chatRoomOptional.isPresent()) {
			List<ChatMessage> chatMessages = chatMessageRepository.findByRoomIdOrderByCreatedAtAsc(chatRoomOptional.get().getRoomId());
			ChatMessageListDto chatMessageListDto = ChatMessageListDto.from(chatMessages);
			return ResponseEntity.ok().body(chatMessageListDto);
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	@GetMapping("/api/user/room/{id}")
	public ResponseEntity<ChatRoomListDto> chatRooms(@PathVariable Long id) {

		Optional<Member> member = memberRepository.findById(id);
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