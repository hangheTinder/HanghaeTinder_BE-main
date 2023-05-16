package com.example.hanghaetinder_bemain.domain.member.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.domain.member.service.ActiveService;
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
	public void likeUsers (@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
		try {
			activeService.likeToUsers(userId, userDetails);
			response.setHeader("Status-Code", "200");
		} catch (Exception e) {
			response.setHeader("Status-Code", "400");
		}
	}

	@Operation(summary = "싫어요 누를시 업데이트", description = "사용자가 싫어요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/dislike/{userId}")
	public void dislikeUsers (@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
		try {
			activeService.dislikeToUsers(userId, userDetails);
			response.setHeader("Status-Code", "200");
		} catch (Exception e) {
			response.setHeader("Status-Code", "400");
		}
	}
}
