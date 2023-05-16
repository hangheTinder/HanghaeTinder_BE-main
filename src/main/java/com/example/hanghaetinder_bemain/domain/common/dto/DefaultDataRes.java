package com.example.hanghaetinder_bemain.domain.common.dto;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultDataRes<T> extends DefaultRes {
	private T data;
	public DefaultDataRes(int statusCode, String responseMessage, T data) {
		super(statusCode, responseMessage);
		this.data = data;
	}
}
