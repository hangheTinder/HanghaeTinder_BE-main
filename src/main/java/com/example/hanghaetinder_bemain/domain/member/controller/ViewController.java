package com.example.hanghaetinder_bemain.domain.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.domain.member.service.ViewService;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ViewController {

	private final ViewService viewService;

	@Operation(summary = "회원목록 전체조회", description = "회원조회 메서드입니다.")
	@GetMapping("/users")

	public ResponseEntity<Message> users (@AuthenticationPrincipal final UserDetailsImpl userDetails){


	}

	@Operation(summary = "좋아요 유저목록", description = "사용자를 좋아요를 누른유저들을 보는메서드입니다.")
	@GetMapping("/users/like")

	public ResponseEntity<Message> likedUser(@AuthenticationPrincipal final UserDetailsImpl userDetails){


	}
}
