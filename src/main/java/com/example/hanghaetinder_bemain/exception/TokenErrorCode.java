package com.example.hanghaetinder_bemain.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenErrorCode implements ErrorCode {

	EXPIRED_ACCESS_TOKEN (HttpStatus.BAD_REQUEST, "만료된 Access token 입니다"),
	EXPIRED_REFRESH_TOKEN (HttpStatus.BAD_REQUEST, "만료된 Refresh token 입니다"),
	;

	private final HttpStatus httpStatus;
	private final String message;
}
