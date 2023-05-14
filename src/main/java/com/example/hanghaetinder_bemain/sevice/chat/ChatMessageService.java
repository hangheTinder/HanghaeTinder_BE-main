package com.example.hanghaetinder_bemain.sevice.chat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.hanghaetinder_bemain.entity.ChatMessage;
import com.example.hanghaetinder_bemain.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
@EnableScheduling
@RequiredArgsConstructor
@Service
public class ChatMessageService {

	private final ChatMessageRepository chatMessageRepository;
	private final RedisTemplate<String, ChatMessage> redisTemplate;

	public void save(ChatMessage message) {
		message.setCreatedAt(new Date());
		redisTemplate.opsForList().rightPush(message.getRoomId().toString(), message);
	}

	@Scheduled(fixedRate  = 60000) // 1분마다 실행
	public void saveMessagesToDb() {
		System.out.println("********************************");
		System.out.println("DB send active");
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
}