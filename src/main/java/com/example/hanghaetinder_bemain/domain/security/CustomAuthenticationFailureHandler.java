package com.example.hanghaetinder_bemain.domain.security;



import com.example.hanghaetinder_bemain.domain.common.dto.DefaultRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		String json = new ObjectMapper().writeValueAsString(new DefaultRes(exception.getMessage()));
		response.getWriter().write(json);
		//        // 로그인 실패 시 처리할 코드 작성하기
		//        response.sendRedirect("/");
	}
}
