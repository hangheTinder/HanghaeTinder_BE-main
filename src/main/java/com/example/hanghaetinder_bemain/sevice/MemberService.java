package com.example.hanghaetinder_bemain.sevice;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.hanghaetinder_bemain.dto.http.ResponseMessage;
import com.example.hanghaetinder_bemain.dto.request.LoginRequestDto;
import com.example.hanghaetinder_bemain.dto.request.SignupRequestDto;
import com.example.hanghaetinder_bemain.dto.resoponse.LoginResponseDto;
import com.example.hanghaetinder_bemain.entity.Favorite;
import com.example.hanghaetinder_bemain.entity.Member;
import com.example.hanghaetinder_bemain.entity.MemberFavorite;
import com.example.hanghaetinder_bemain.exception.CustomException;
import com.example.hanghaetinder_bemain.jwt.JwtUtil;
import com.example.hanghaetinder_bemain.repository.FavoriteRepository;
import com.example.hanghaetinder_bemain.repository.MemberRepository;
import com.example.hanghaetinder_bemain.util.AgeCalculator;
import com.example.hanghaetinder_bemain.util.S3Uploader;

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
	private final RedisService redisService;
	private final S3Uploader s3Uploader;
	private final AmazonS3Client amazonS3Client;


	@Transactional
	public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
		String userId = loginRequestDto.getUserId();
		String password = loginRequestDto.getPassword();

		Member member = memberRepository.findByUserId(userId).orElseThrow(
			() -> new CustomException(ResponseMessage.NOT_FOUND_USER));// 예외처리 해주기

		if (!passwordEncoder.matches(password, member.getPassword())) {
			throw new CustomException(ResponseMessage.NOT_FOUND_USER);
		}

		String accessToken = jwtUtil.createAccessToken(member.getUserId());
		String refreshToken = jwtUtil.createRefreshToken(member.getUserId());

		String redisKey = refreshToken.substring(7);
		redisService.setValues(redisKey, member.getUserId());

		response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
		response.addHeader(JwtUtil.REFRESHTOKEN_HEADER, refreshToken);

		return new LoginResponseDto(member.getNickname());
	}

	@Transactional(readOnly = true)
	public void logout(HttpServletRequest request) {
		String refreshToken = request.getHeader(JwtUtil.REFRESHTOKEN_HEADER).substring(7);

		if (refreshToken != null && redisService.getValues(refreshToken) != null) {
			redisService.deleteValues(refreshToken);
		} else {
			throw new CustomException(ResponseMessage.WRONG_ACCESS);
		}
	}

	//회원가입
	@Transactional
	public void signup(SignupRequestDto signupRequestDto) {

		if (signupRequestDto.getUserId() == null || signupRequestDto.getPassword() == null || signupRequestDto.getNickname() == null || signupRequestDto.getBirth() == null || signupRequestDto.getGender() == null || signupRequestDto.getImage() == null ) {
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


	}


}
