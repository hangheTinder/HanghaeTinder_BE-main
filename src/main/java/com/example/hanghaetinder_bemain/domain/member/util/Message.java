package com.example.hanghaetinder_bemain.domain.member.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
	private int statusCode;
	private String message;
	private Object data;

	public Message(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public static Message setSuccess(StatusEnum statusEnum, String message, Object data) {
		return new Message(statusEnum.getStatus().value(), message, data);
	}

	public static Message setSuccess(StatusEnum statusEnum, String message) {
		return new Message(statusEnum.getStatus().value(), message);
	}
}