package com.example.hanghaetinder_bemain.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hanghaetinder_bemain.dto.http.DefaultDataRes;
import com.example.hanghaetinder_bemain.dto.http.DefaultRes;
import com.example.hanghaetinder_bemain.dto.http.ResponseMessage;
import com.example.hanghaetinder_bemain.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.dto.resoponse.LoginResponseDto;
import com.example.hanghaetinder_bemain.sevice.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

	private final MemberService memberService;

	//회원가입
	@PostMapping(value = "/signup" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity signup(@ModelAttribute SignupRequestDto signUpRequestDto) {

		memberService.signup(signUpRequestDto);

		return ResponseEntity.ok(new DefaultRes(ResponseMessage.CREATED_USER));
	}


	@PostMapping("/login")
	public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		LoginResponseDto loginResponseDto = memberService.login(loginRequestDto, response);
		return ResponseEntity.ok(new DefaultDataRes<>(ResponseMessage.LOGIN_SUCCESS, loginResponseDto));
	}

	@GetMapping("/logout")
	public ResponseEntity logout(HttpServletRequest request) {
		memberService.logout(request);
		return ResponseEntity.ok(new DefaultRes(ResponseMessage.LOGOUT_SUCCESS));
	}






}
