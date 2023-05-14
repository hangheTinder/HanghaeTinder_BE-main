package com.example.hanghaetinder_bemain.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.example.hanghaetinder_bemain.entity.Gender;

@Getter
@Setter
public class SignupRequestDto {

	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "유효한 이메일 주소를 입력하세요.")
	private String userId;

	@Pattern(regexp = "^[a-zA-Z0-9@$!%*#?&]{8,20}$", message = "password는 영문, 숫자, 특수문자로 8자~20자 이하만 가능합니다.")
	private String password;

	private String nickname;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate birth;

	private List<String> favorites;
	private Gender gender;
	private MultipartFile image;



}
