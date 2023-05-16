package com.example.hanghaetinder_bemain.domain.member.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanghaetinder_bemain.domain.member.entity.Gender;

@Getter
@Setter
public class SignupRequestDto {


	@Email
	@Pattern(regexp = "^[a-z0-9_+.-]+@[a-z0-9-]+\\.[a-z0-9]{2,4}$", message = "아이디는 올바른 이메일 형식으로 입력해주세요.")

	private String userId;

	@Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,15}$", message = "password는 영문, 숫자, 특수문자로 8자~20자 이하만 가능합니다.")
	private String password;

	private String nickname;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birth;

	private List<String> favorites;
	private Gender gender;
	private MultipartFile image;



}
