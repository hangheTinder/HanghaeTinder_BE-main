package com.example.hanghaetinder_bemain.domain.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
	public ResponseEntity signup(@Valid SignupRequestDto signUpRequestDto, Errors errors) {

		if (errors.hasErrors()) {
			// 유효성 검사 실패 시 처리
			return ResponseEntity.badRequest().body(new DefaultRes(400, "회원가입폼을 올바르게 작성해주세요."));
		}

		memberService.signup(signUpRequestDto);

		return ResponseEntity.ok(new DefaultRes(200,ResponseMessage.CREATED_USER));
	}

	@PostMapping("user/login")
	public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
		LoginResponseDto loginResponseDto = memberService.login(loginRequestDto, response);
		return ResponseEntity.ok(new DefaultDataRes<>(200,ResponseMessage.LOGIN_SUCCESS, loginResponseDto));
	}

}

