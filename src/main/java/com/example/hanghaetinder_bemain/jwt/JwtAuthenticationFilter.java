package com.example.hanghaetinder_bemain.jwt;
import com.example.hanghaetinder_bemain.dto.http.DefaultRes;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

		String accessToken = jwtUtil.resolveAccessToken(request);
		String refreshToken = jwtUtil.resolveRefreshToken(request);

		if (refreshToken != null && accessToken != null) {
			if (jwtUtil.existsRefreshToken(refreshToken) && jwtUtil.validateToken(refreshToken, jwtUtil.getRefreshKey())) {
				if (jwtUtil.validateToken(accessToken, jwtUtil.getAccessKey())) {
					String userId = jwtUtil.getUserInfoFromToken(jwtUtil.getRefreshKey(), refreshToken).getSubject();
					if (jwtUtil.isExpired(jwtUtil.getAccessKey(), accessToken)) { // 만료됏으면
						/// 토큰 발급
						String newAccessToken = jwtUtil.createAccessToken(userId);
						/// 헤더에 어세스 토큰 추가
						jwtUtil.setHeaderAccessToken(response, newAccessToken);
					}

					try {
						this.setAuthentication(userId);
					} catch (UsernameNotFoundException e) {
						jwtExceptionHandler(response, e.getMessage(), HttpStatus.UNAUTHORIZED.value());
						return;
					}
				} else {
					jwtExceptionHandler(response, "유효하지 않은 액세스 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
					return;
				}
			} else {
				jwtExceptionHandler(response, "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED.value());
				return;
			}

		}
		filterChain.doFilter(request, response);

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

