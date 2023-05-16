package com.example.hanghaetinder_bemain.domain.security.config;

import com.example.hanghaetinder_bemain.domain.security.jwt.JwtAuthenticationFilter;
import com.example.hanghaetinder_bemain.domain.security.jwt.JwtUtil;
import com.example.hanghaetinder_bemain.domain.security.CustomAuthenticationEntryPoint;
import com.example.hanghaetinder_bemain.domain.security.CustomAuthenticationFailureHandler;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

	private static final String[] AUTH_WHITELIST = {
		"/api/user/**",
		"/member/authenticate",
		"/swagger-ui.html",
		"/swagger-ui/**",
		"/v3/api-docs/**",
		"/swagger-resources/**",
		"/api-docs",
		"/api/room/**",
	};
	private final JwtUtil jwtUtil;
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		// CSRF 설정
		http.csrf().disable();

		http.cors();
		// 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeHttpRequests(authorize -> authorize
				.shouldFilterAllDispatcherTypes(false)
				.antMatchers(AUTH_WHITELIST)
				.permitAll()
				.antMatchers("/chat/**").permitAll()
				.anyRequest()
				.authenticated()) // 그외의 요청들은 모두 인가 받아야 한다.
			// JWT 인증/인가를 사용하기 위한 설정
			.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		// 401 에러 핸들링
		http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);

		//SockJS를 위해
		http.headers().frameOptions().sameOrigin();
		http.formLogin().failureHandler(customAuthenticationFailureHandler).permitAll();

		return http.build();
	}

	// cors 에러를 위한 체크
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.addAllowedOrigin("http://localhost:3000");
		config.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);
		config.addAllowedMethod("*");
		config.addAllowedHeader("*");
		config.setAllowCredentials(true);
		config.validateAllowCredentials();

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}


	// 비밀번호 암호화
	@Bean // 비밀번호 암호화 기능 등록
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
