package com.example.hanghaetinder_bemain.domain.chat.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.domain.chat.dto.response.ChatRoomListDto;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatMessage;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;
import com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage;
import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;
import com.example.hanghaetinder_bemain.domain.member.repository.MatchMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.member.util.StatusEnum;

import lombok.RequiredArgsConstructor;
@EnableScheduling
@RequiredArgsConstructor
@Service
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final RedisTemplate<String, ChatMessage> redisTemplate;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final MatchMemberRepository matchMemberRepository;
	private final SimpMessageSendingOperations messagingTemplate;


	public void save(ChatMessage message) {
		redisTemplate.opsForList().rightPush(message.getRoomId().toString(), message);
	}


	@Scheduled(fixedRate  = 60000) // 1분마다 실행
	public void saveMessagesToDb() {
		// Redis에 저장된 메세지를 DB에 저장하는 작업 수행
		// 예를 들어, Redis에서 모든 메세지를 가져와서 DB에 저장하는 코드를 추가할 수 있습니다.
		// 이 부분은 적절히 구현해주세요.
		Set<String> roomIdKeys = redisTemplate.keys("*"); // Redis에 저장된 모든 채팅방 키를 가져옴

		for (String roomIdKey : roomIdKeys) {
			List<ChatMessage> chatMessages = redisTemplate.opsForList().range(roomIdKey, 0, -1);
			chatMessageRepository.saveAll(chatMessages);
			redisTemplate.delete(roomIdKey);
		}
	}
	@Async
	public CompletableFuture<Void> updateChatRoomListAsync(ChatMessage message) {
		Optional<Member> member = memberRepository.findByUserId(message.getUserId());
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
			messagingTemplate.convertAndSend("/sub/chat/rooms/" + message.getUserId(), msg);
		}
		else{
			ChatRoomListDto chatRoomListDto = new ChatRoomListDto();
			Message msg = Message.setSuccess(StatusEnum.OK, "조회 성공", chatRoomListDto);
			messagingTemplate.convertAndSend("/sub/chat/rooms/" + message.getRoomId(), msg);
		}
		return CompletableFuture.completedFuture(null);
	}


}