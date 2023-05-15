package com.example.hanghaetinder_bemain.dto.http;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultDataRes<T> extends DefaultRes {
	private T data;

	public DefaultDataRes(String responseMessage, T data) {
		super(responseMessage);
		this.data = data;
	}
}
