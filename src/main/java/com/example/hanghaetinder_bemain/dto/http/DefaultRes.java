package com.example.hanghaetinder_bemain.dto.http;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultRes {
	private String responseMessage;

	public DefaultRes(final String responseMessage) {
		this.responseMessage = responseMessage;
	}
}

