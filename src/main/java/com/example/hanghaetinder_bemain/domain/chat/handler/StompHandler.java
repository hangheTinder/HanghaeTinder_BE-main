package com.example.hanghaetinder_bemain.domain.chat.handler;

import java.security.Principal;
import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.example.hanghaetinder_bemain.domain.security.jwt.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor {

	private final JwtUtil jwtTokenProvider;

	// websocket을 통해 들어온 요청이 처리 되기전 실행된다.
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		// websocket 연결시 헤더의 jwt token 검증
		if (StompCommand.CONNECT == accessor.getCommand()) {
			String token = accessor.getFirstNativeHeader("Authorization").substring(7);;
			if (token != null) {
				jwtTokenProvider.validateToken(token);
			} else {
				System.out.println("Token is null in the header");
			}
		}

		return message;
	}


}