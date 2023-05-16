package com.example.hanghaetinder_bemain.domain.member.entity;

public enum Gender {
	MALE("남성"),
	FEMALE("여성");

	private String value;

	Gender(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
