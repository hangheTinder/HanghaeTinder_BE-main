package com.example.hanghaetinder_bemain.domain.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.hanghaetinder_bemain.domain.common.dto.DefaultDataRes;
import com.example.hanghaetinder_bemain.domain.common.dto.DefaultRes;
import com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage;
import com.example.hanghaetinder_bemain.domain.member.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.domain.member.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.LoginResponseDto;
import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.security.UserDetailsImpl;
import com.example.hanghaetinder_bemain.domain.member.service.MemberService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

	private final MemberService memberService;
	//회원가입
	@PostMapping(value = "user/signup" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Message> signup(@ModelAttribute final SignupRequestDto signUpRequestDto) {

		return memberService.signup(signUpRequestDto);
	}

	@PostMapping("user/login")
	public ResponseEntity<Message> login(@RequestBody final LoginRequestDto loginRequestDto, final HttpServletResponse  response) {

		return	memberService.login(loginRequestDto, response);
	}

}

