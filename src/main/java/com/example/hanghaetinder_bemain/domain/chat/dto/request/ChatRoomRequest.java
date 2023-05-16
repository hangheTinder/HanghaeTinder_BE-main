package com.example.hanghaetinder_bemain.domain.chat.dto.request;

import lombok.Getter;

@Getter
public class ChatRoomRequest {
	private Long matchMemberId;

	public ChatRoomRequest(Long matchMemberId) {
		this.matchMemberId = matchMemberId;
	}

	public Long getMatchMemberId() {
		return matchMemberId;
	}

	public void setMatchMemberId(Long matchMemberId) {
		this.matchMemberId = matchMemberId;
	}
}
