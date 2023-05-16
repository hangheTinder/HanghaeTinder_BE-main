package com.example.hanghaetinder_bemain.domain.member.service;

import static org.junit.Assert.*;

import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;

public class ViewServiceTest {

	@org.junit.Test
	public void users() {

		UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);
		System.out.println("Mock data: "+ userDetails);
		Mockito.when(userDetails.getId()).thenReturn(1L);

		ViewService viewService = Mockito.mock(ViewService.class);
		long startTime = System.currentTimeMillis();
		ResponseEntity<Message> response = viewService.users(userDetails);

		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		System.out.println("users() 쿼리 실행 시간: " + executionTime + "ms");
	}

	@org.junit.Test
	public void likedUser() {
		long startTime = System.currentTimeMillis();


		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;
		System.out.println("likeedUser() 쿼리 실행 시간: " + executionTime + "ms");

	}
}