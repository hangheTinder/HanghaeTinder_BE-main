package com.example.hanghaetinder_bemain.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.repository.MemberRepository;
import com.example.hanghaetinder_bemain.security.auth.UserDetailsImpl;
import com.example.hanghaetinder_bemain.sevice.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;
	private final MemberRepository memberRepository;


	@Operation(summary = "회원목록 전체조회", description = "회원조회 메서드입니다.")
	@GetMapping("/users")
	public List<MemberResponseDto> users(@AuthenticationPrincipal UserDetailsImpl userDetails){


		return memberService.users(userDetails);
	}

	@Operation(summary = "채팅방 목록 조회", description = "채팅방 목록가기 클릭시 실행 메서드입니다.")
	@GetMapping("/users/match")
	public List<ChatRoom> getChatRooms(@AuthenticationPrincipal UserDetailsImpl userDetails){
		List<MemberResponseDto> matchedUsers = memberService.matched(userDetails);
		return memberService.getChatRooms(matchedUsers, userDetails);
	}

	@Operation(summary = "좋아요 누를시 업데이트", description = "사용자가 좋아요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/like/{userId}")
	public void likeUsers(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){

		memberService.likeToUsers(userId,userDetails);
	}
	@Operation(summary = "싫어요 누를시 업데이트", description = "사용자가 싫어요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/dislike/{userId}")
	public void dislikeUsers(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails){

		memberService.dislikeToUsers(userId,userDetails);
	}




}
