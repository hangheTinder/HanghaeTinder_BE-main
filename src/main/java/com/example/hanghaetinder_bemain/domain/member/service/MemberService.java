package com.example.hanghaetinder_bemain.domain.member.service;

import java.io.IOException;

import java.util.Date;
import java.util.HashMap;

import java.util.List;

import javax.servlet.http.HttpServletResponse;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage;
import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;
import com.example.hanghaetinder_bemain.domain.member.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.domain.member.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.LoginResponseDto;
import com.example.hanghaetinder_bemain.domain.member.entity.Favorite;
import com.example.hanghaetinder_bemain.domain.member.entity.Member;

import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberFavoriteRepository;
import com.example.hanghaetinder_bemain.domain.member.util.Message;
import com.example.hanghaetinder_bemain.domain.member.util.StatusEnum;
import com.example.hanghaetinder_bemain.domain.security.jwt.JwtUtil;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatMessageRepository;
import com.example.hanghaetinder_bemain.domain.chat.repository.ChatRoomRepository;

import com.example.hanghaetinder_bemain.domain.member.repository.FavoriteRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.MemberRepository;
import com.example.hanghaetinder_bemain.domain.member.util.AgeCalculator;
import com.example.hanghaetinder_bemain.domain.member.util.S3Uploader;
 
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import com.example.hanghaetinder_bemain.domain.member.dto.resoponse.MemberResponseDto;
import com.example.hanghaetinder_bemain.domain.chat.entity.ChatRoom;
import com.example.hanghaetinder_bemain.domain.member.entity.DislikeMember;
import com.example.hanghaetinder_bemain.domain.member.entity.LikeMember;
import com.example.hanghaetinder_bemain.domain.member.repository.DislikeMemberRepository;
import com.example.hanghaetinder_bemain.domain.member.repository.LikeMemberRepository;

import com.example.hanghaetinder_bemain.domain.security.jwt.JwtUtil;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	private final FavoriteRepository favoriteRepository;
	private final PasswordEncoder passwordEncoder;
	private final S3Uploader s3Uploader;

	@Transactional

	public ResponseEntity<Message> login(final LoginRequestDto loginRequestDto,final HttpServletResponse response) {


		String userId = loginRequestDto.getUserId();
		String password = loginRequestDto.getPassword();

		Member member = memberRepository.findByUserId(userId).orElseThrow(
			() -> new CustomException(ResponseMessage.NOT_FOUND_USER));// 예외처리 해주기

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ResponseMessage.NOT_Fail_USER);
		}

		String accessToken = jwtUtil.createAccessToken(member.getUserId());

		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		// System.out.println(accessToken);
		Map<String, String> map = new HashMap<>();
		map.put("nickname", member.getNickname());

		Message message = Message.setSuccess(StatusEnum.OK, "로그인 성공", map);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	//회원가입
	@Transactional

	public ResponseEntity<Message> signup(final SignupRequestDto signupRequestDto) {


		if (signupRequestDto.getUserId() == null ||
			signupRequestDto.getPassword() == null ||
			signupRequestDto.getNickname() == null ||
			signupRequestDto.getBirth() == null ||
			signupRequestDto.getGender() == null ||
			signupRequestDto.getImage() == null ) {
			throw new CustomException(ResponseMessage.WRONG_FORMAT);
		}
		if (memberRepository.existsByUserId(signupRequestDto.getUserId())) {
			throw new CustomException(ResponseMessage.ALREADY_ENROLLED_USER);
		}

		Member member = new Member(signupRequestDto);
		String password = passwordEncoder.encode(signupRequestDto.getPassword());
		member.setPassword(password);
		List<Favorite> favorites = favoriteRepository.findAllByFavoriteNameIn(signupRequestDto.getFavorites());
		member.setFavorites(favorites);


		try { // upload method 에서 발생하는 IOException 을 Customize 하기 위해 try-catch 사용
			String imgPath = s3Uploader.upload(signupRequestDto.getImage());

			member.setImg(imgPath);

		} catch (IOException e) {
			throw new CustomException(ResponseMessage.S3_ERROR);
		}

		int age = AgeCalculator.calculateAge(signupRequestDto.getBirth());

		member.setAge(age);

		memberRepository.save(member);
		Message message = Message.setSuccess(StatusEnum.OK, "회원가입 성공");
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

}
