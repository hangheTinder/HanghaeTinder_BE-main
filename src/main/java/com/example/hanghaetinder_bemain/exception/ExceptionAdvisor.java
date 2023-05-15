package com.example.hanghaetinder_bemain.exception;




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

import com.example.hanghaetinder_bemain.dto.http.DefaultDataRes;
import com.example.hanghaetinder_bemain.dto.http.DefaultRes;
import com.example.hanghaetinder_bemain.dto.http.ResponseMessage;

@ControllerAdvice
@RestController
public class ExceptionAdvisor {

	// CustomException 처리
	@ExceptionHandler(value = {CustomException.class})
	public ResponseEntity<Object> handleCustomException(CustomException ex) {
		String msg = ex.getMsg();

		return ResponseEntity.status(200).body(new DefaultRes(msg));
	}

	// 아이디, 비밀번호 유효성 검사 시 에러
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<?> handleValidException(MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		Map<String, String> errorMap = new HashMap<>();
		if (bindingResult.hasErrors()) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
			}
		}
		return ResponseEntity.badRequest().body(new DefaultDataRes<Map<String, String>>(ResponseMessage.CREATED_USER_FAIL, errorMap));
	}

	// 이미지 파일 미 업로드 시
	@ExceptionHandler(value = {BindException.class})
	public ResponseEntity<?> handleBindException(BindException ex) {
		return ResponseEntity.badRequest().body(new DefaultRes(ResponseMessage.WRONG_FORMAT));
	}
}
