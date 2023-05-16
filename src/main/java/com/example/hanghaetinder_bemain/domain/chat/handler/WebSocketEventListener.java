package com.example.hanghaetinder_bemain.domain.chat.handler;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
		// 세션 아이디를 기반으로 연결된 클라이언트의 정보를 처리합니다.
		// ...
	}
}