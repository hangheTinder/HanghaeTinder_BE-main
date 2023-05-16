package com.example.hanghaetinder_bemain.domain.common.dto;


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

