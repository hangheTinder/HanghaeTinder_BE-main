package com.example.hanghaetinder_bemain.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

	RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 글은 존재하지 않습니다"),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버에서 문제가 발생했습니다"),
	IO_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장 중 내부 서버에서 문제가 발생했습니다"),
	INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "유효하지 않은 파라미터 입니다")
	;

	private final HttpStatus httpStatus;
	private final String message;
}
