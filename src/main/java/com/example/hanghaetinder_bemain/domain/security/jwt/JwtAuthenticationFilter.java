package com.example.hanghaetinder_bemain.domain.security.jwt;
import com.example.hanghaetinder_bemain.domain.common.dto.DefaultRes;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String token = jwtUtil.resolveAccessToken(request);

		if(token != null) {
			if(!jwtUtil.validateToken(token)){
				jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
				return;
			}
			Claims info = jwtUtil.getUserInfoFromToken(token);
			setAuthentication(info.getSubject());
		}
		filterChain.doFilter(request,response);
	}


	public void setAuthentication(String userId) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(userId);
		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);
	}

	public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			String json = new ObjectMapper().writeValueAsString(new DefaultRes(msg));
			response.getWriter().write(json);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

}

