package com.example.hanghaetinder_bemain.domain.common.exception;




import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import com.example.hanghaetinder_bemain.domain.common.dto.DefaultDataRes;
import com.example.hanghaetinder_bemain.domain.common.dto.DefaultRes;
import com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage;

@ControllerAdvice
@RestController
public class ExceptionAdvisor {
	// CustomException 처리
	@ExceptionHandler(value = {CustomException.class})
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		String msg = ex.getMsg();

		return ResponseEntity.status(400).body(new DefaultRes(400, msg));
	}

	// 아이디, 비밀번호 유효성 검사 시 에러
	@ExceptionHandler(value = {ConstraintViolationException.class})
	public ResponseEntity<?> handleValidException(ConstraintViolationException ex) {
		return ResponseEntity.badRequest().body(new DefaultRes(400, ex.getMessage()));
	}



	// 이미지 파일 미 업로드 시
	@ExceptionHandler(value = {BindException.class})
	public ResponseEntity<?> handleBindException(BindException ex) {
		return ResponseEntity.badRequest().body(new DefaultRes(400, ex.getMessage()));
	}
}
