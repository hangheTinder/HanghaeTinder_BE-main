package com.example.hanghaetinder_bemain.domain.member.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StatusEnum {

	NOT_FOUND(HttpStatus.NOT_FOUND,"NOT_FOUND"),
	OK(HttpStatus.OK, "OK");
	private final HttpStatus status;
	private final String message;
}