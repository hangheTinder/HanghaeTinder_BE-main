package com.example.hanghaetinder_bemain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestApiException extends RuntimeException {

	private final ErrorCode errorCode;
}
