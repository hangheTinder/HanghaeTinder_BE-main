package com.example.hanghaetinder_bemain.domain.member.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.domain.member.service.ActiveService;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ActiveController {

	private final ActiveService activeService;
	@Operation(summary = "좋아요 누를시 업데이트", description = "사용자가 좋아요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/like/{userId}")
	public ResponseEntity<Message> likeUsers (@PathVariable final Long userId, @AuthenticationPrincipal final UserDetailsImpl userDetails){

		return activeService.likeToUsers(userId, userDetails);

	}

	@Operation(summary = "싫어요 누를시 업데이트", description = "사용자가 싫어요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/dislike/{userId}")
	public ResponseEntity<Message> dislikeUsers (@PathVariable final Long userId, @AuthenticationPrincipal final UserDetailsImpl userDetails){

		return activeService.dislikeToUsers(userId, userDetails);
	}
}
