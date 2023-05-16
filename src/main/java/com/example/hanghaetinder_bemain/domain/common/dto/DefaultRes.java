package com.example.hanghaetinder_bemain.domain.common.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultRes {
	private String message;

	private int statusCode;

	public DefaultRes(final int statusCode,final String responseMessage) {
		this.statusCode = statusCode;
		this.message = responseMessage;
	}
}

