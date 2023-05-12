package com.example.hanghaetinder_bemain.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

	String name();
	HttpStatus getHttpStatus();
	String getMessage();
}
