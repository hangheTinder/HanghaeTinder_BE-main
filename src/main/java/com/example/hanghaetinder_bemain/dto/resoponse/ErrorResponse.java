package com.example.hanghaetinder_bemain.dto.resoponse;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

	private final String errorCode;
	private final String status;
	private final String message;
}
