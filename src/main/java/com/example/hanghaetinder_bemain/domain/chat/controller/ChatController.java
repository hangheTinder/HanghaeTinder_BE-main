package com.example.hanghaetinder_bemain.domain.chat.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.joda.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.example.hanghaetinder_bemain.domain.member.service.ActiveService;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.member.util.StatusEnum;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.domain.member.service.MemberService;
import com.example.hanghaetinder_bemain.domain.chat.service.ChatMessageService;

import io.swagger.v3.oas.annotations.Operation;
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
	private final ActiveService activeService;

	@Transactional
	@MessageMapping("/chat/message")
	public void message(ChatMessage message) {

		switch (message.getType()){
			case ROOM:
				System.out.println("****room**");
				System.out.println("***get roomId"+message.getRoomId());
				Optional<Member> member = memberRepository.findByUserId(message.getRoomId());
				List<ChatRoom> matchMemberOptional = matchMemberRepository.findMatchmember(member.get().getId());
				if (!matchMemberOptional.isEmpty()) {
					List<ChatRoomListDto.ChatRoomDto> chatRoomDtos = matchMemberOptional.stream()
						.map(chatRoom -> {
							List<ChatMessage> latestMessages = chatMessageRepository.findLatestMessageByRoomId(chatRoom.getRoomId(), PageRequest.of(0, 1));
							ChatMessage latestMessage = latestMessages.isEmpty() ? null : latestMessages.get(0);
							return new ChatRoomListDto.ChatRoomDto(
								chatRoom.getId(),
								chatRoom.getName(),
								chatRoom.getRoomId(),
								latestMessage != null ? latestMessage.getCreatedAt() : null,
								latestMessage != null ? latestMessage.getMessage() : null
							);
						})
						.collect(Collectors.toList());
					ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoomDtos);
					Message msg = Message.setSuccess(StatusEnum.OK, "조회 성공", chatRoomListDto);
					messagingTemplate.convertAndSend("/sub/chat/rooms/" + message.getRoomId(), msg);
				}
				else{
					ChatRoomListDto chatRoomListDto = new ChatRoomListDto();
					Message msg = Message.setSuccess(StatusEnum.OK, "조회 성공", chatRoomListDto);
					messagingTemplate.convertAndSend("/sub/chat/rooms/" + message.getRoomId(), msg);
				}
				break;
			case ENTER:
				System.out.println("****enter**");
				System.out.println("***get roomId"+message.getRoomId());
				Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomId(message.getRoomId());
				if (chatRoomOptional.isPresent()) {
					Pageable pageable = PageRequest.of(message.getPage(), 10, Sort.by("createdAt").descending());
					Page<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(chatRoomOptional.get().getRoomId(), pageable);
					ChatMessageListDto chatMessageListDto = ChatMessageListDto.from(chatMessages);
					Message msg = Message.setSuccess(StatusEnum.OK, "조회 성공", chatMessageListDto);
					messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), msg);
				}
				break;

			case TALK:
				Optional<Member> nickname = memberRepository.findByUserId(message.getUserId());
				ChatMessageListDto.ChatMessageDto messageDto = new ChatMessageListDto.ChatMessageDto(nickname.get().getUserId(), message.getMessage(), new Date());
				ChatRoom chatRoom = chatRoomRepository.findRoomId(message.getRoomId());
				ChatMessage chatMessage = new ChatMessage(message.getType(), message.getRoomId(), nickname.get().getUserId(), message.getMessage(), new Date(), chatRoom);
				messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), messageDto);
				chatMessageService.updateChatRoomListAsync(message);
				chatMessageService.save(chatMessage);
				break;

			default:
				break;
		}
	}

	@Operation(summary = "채팅방에서 사용자가 싫어요 누르고 나갔을때", description = "사용자가 싫어요를 하고 실행됬을 때 입니다")
	@PostMapping("/api/users/dislike/{userId}/{roomId}")
	public ResponseEntity<Message> dislikeUsers (@PathVariable final String userId, @PathVariable final String roomId){

		return activeService.dislikeToUsersByRoom(userId, roomId);


	}

	@GetMapping("/api/user/{Rid}/messages")
	public ResponseEntity<ChatMessageListDto> roomMessages(@PathVariable String Rid, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {

		Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByRoomId(Rid);
		if (chatRoomOptional.isPresent()) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
			Page<ChatMessage> chatMessages = chatMessageRepository.findByRoomId(chatRoomOptional.get().getRoomId(), pageable);

			ChatMessageListDto chatMessageListDto = ChatMessageListDto.from(chatMessages);
			return ResponseEntity.ok().body(chatMessageListDto);
		}
		return ResponseEntity.notFound().build();
	}

	@Transactional
	@GetMapping("/api/user/room")
	public ResponseEntity<Message> chatRooms(@AuthenticationPrincipal final UserDetailsImpl userDetails) {

		Optional<Member> member = memberRepository.findById(userDetails.getId());
		List<ChatRoom> matchMemberOptional = matchMemberRepository.findMatchmember(member.get().getId());

		if (!matchMemberOptional.isEmpty()) {
			List<ChatRoomListDto.ChatRoomDto> chatRoomDtos = matchMemberOptional.stream()
				.map(chatRoom -> {
					List<ChatMessage> latestMessages = chatMessageRepository.findLatestMessageByRoomId(chatRoom.getRoomId(), PageRequest.of(0, 1));
					ChatMessage latestMessage = latestMessages.isEmpty() ? null : latestMessages.get(0);
					return new ChatRoomListDto.ChatRoomDto(
						chatRoom.getId(),
						chatRoom.getName(),
						chatRoom.getRoomId(),
						latestMessage != null ? latestMessage.getCreatedAt() : null,
						latestMessage != null ? latestMessage.getMessage() : null
					);
				})
				.collect(Collectors.toList());
			ChatRoomListDto chatRoomListDto = new ChatRoomListDto(chatRoomDtos);
			Message message = Message.setSuccess(StatusEnum.OK, "조회 성공", chatRoomListDto);
			return new ResponseEntity<>(message, HttpStatus.OK);
		}

		Message message = Message.setSuccess(StatusEnum.OK, "조회 결과 없음");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}


}