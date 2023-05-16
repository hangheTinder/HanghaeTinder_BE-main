package com.example.hanghaetinder_bemain.domain.common.exception;


public class CustomException extends RuntimeException{
	private String msg;

	public CustomException(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}
}
