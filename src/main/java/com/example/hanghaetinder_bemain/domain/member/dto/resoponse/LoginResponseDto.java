package com.example.hanghaetinder_bemain.domain.member.dto.resoponse;

import lombok.Getter;

@Getter
public class LoginResponseDto {
	private String nickname;

	public LoginResponseDto(String nickname) {
		this.nickname = nickname;
	}
}
