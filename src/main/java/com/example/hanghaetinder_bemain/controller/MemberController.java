package com.example.hanghaetinder_bemain.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.catalina.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.hanghaetinder_bemain.dto.http.DefaultDataRes;
import com.example.hanghaetinder_bemain.dto.http.DefaultRes;
import com.example.hanghaetinder_bemain.dto.http.ResponseMessage;
import com.example.hanghaetinder_bemain.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.dto.resoponse.LoginResponseDto;
import com.example.hanghaetinder_bemain.dto.resoponse.MemberResponseDto;
// import com.example.hanghaetinder_bemain.entity.ChatRoom;
import com.example.hanghaetinder_bemain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.sevice.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;

	//회원가입
	@PostMapping(value = "user/signup" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity signup(@ModelAttribute SignupRequestDto signUpRequestDto) {

		memberService.signup(signUpRequestDto);

		return ResponseEntity.ok(new DefaultRes(ResponseMessage.CREATED_USER));
	}

	@PostMapping("user/login")
	public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		LoginResponseDto loginResponseDto = memberService.login(loginRequestDto, response);
		return ResponseEntity.ok(new DefaultDataRes<>(ResponseMessage.LOGIN_SUCCESS, loginResponseDto));
	}

	@GetMapping("/user/login-page")
	public ModelAndView loginPage() {
		return new ModelAndView("/chat/login.html");
	}

	// @GetMapping("user/logout")
	// public ResponseEntity logout(HttpServletRequest request) {
	// 	memberService.logout(request);
	// 	return ResponseEntity.ok(new DefaultRes(ResponseMessage.LOGOUT_SUCCESS));
	// }


	@Operation(summary = "회원목록 전체조회", description = "회원조회 메서드입니다.")
	@GetMapping("/users")
	public List<MemberResponseDto> users (@AuthenticationPrincipal final UserDetailsImpl userDetails){

		return memberService.users(userDetails);
	}

	@Operation(summary = "좋아요 누를시 업데이트", description = "사용자가 좋아요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/like/{userId}")
	public void likeUsers (@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
		try {
			memberService.likeToUsers(userId, userDetails);
			response.setHeader("Status-Code", "200");
		} catch (Exception e) {
			response.setHeader("Status-Code", "400");
		}
	}

	@Operation(summary = "싫어요 누를시 업데이트", description = "사용자가 싫어요를 눌렀을때 실행되는 메서드입니다.")
	@PostMapping("/users/dislike/{userId}")
	public void dislikeUsers (@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){
		try {
			memberService.dislikeToUsers(userId, userDetails);
			response.setHeader("Status-Code", "200");
		} catch (Exception e) {
			response.setHeader("Status-Code", "400");
		}
	}
	@Operation(summary = "좋아요 유저목록", description = "사용자를 좋아요를 누른유저들을 보는메서드입니다..")
	@GetMapping("/users/like/")
	public List<MemberResponseDto> likedUser(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response){

		response.setHeader("Status-Code", "200");
		return memberService.likedUser(userDetails);
	}
}

