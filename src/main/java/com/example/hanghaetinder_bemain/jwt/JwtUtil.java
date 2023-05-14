package com.example.hanghaetinder_bemain.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import com.example.hanghaetinder_bemain.security.UserDetailsServiceImpl;
import com.example.hanghaetinder_bemain.sevice.RedisService;

@Slf4j
@Component
@RequiredArgsConstructor
@Getter
public class JwtUtil {

	private final UserDetailsServiceImpl userDetailsService;
	private final RedisService redisService;
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESHTOKEN_HEADER = "RefreshToken";
	public static final String AUTHORIZATION_KEY = "auth";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final long TOKEN_TIME = 600000L; // 10분으로 설정 600000L

	private static final long REFRESH_TOKEN_TIME = 6 * 1800000L; // 3시간으로 설정 6 * 1800000L

	@Value("${jwt.secret.access-key}")
	private String accessSecretKey;
	@Value("${jwt.secret.refresh-key}")
	private String refreshSecretKey;
	private Key accessKey;
	private Key refreshKey;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(accessSecretKey); //Base64로 인코딩되어 있는 것을, 값을 가져와서(getDecoder()) 디코드하고(decode(secretKey)), byte 배열로 반환
		accessKey = Keys.hmacShaKeyFor(bytes); //반환된 bytes 를 hmacShaKeyFor() 메서드를 사용해서 Key 객체에 넣기

		bytes = Base64.getDecoder().decode(refreshSecretKey); //Base64로 인코딩되어 있는 것을, 값을 가져와서(getDecoder()) 디코드하고(decode(secretKey)), byte 배열로 반환
		refreshKey = Keys.hmacShaKeyFor(bytes); //반환된 bytes 를 hmacShaKeyFor() 메서드를 사용해서 Key 객체에 넣기
	}

	//Access Token 생성
	public String createAccessToken(String userId) {
		return BEARER_PREFIX + this.createToken(userId, accessKey, TOKEN_TIME);
	}

	//Refresh Token 생성
	public String createRefreshToken(String userId) {
		return BEARER_PREFIX + this.createToken(userId, refreshKey, REFRESH_TOKEN_TIME);
	}

	public String createToken(String userId, Key key, long tokenValid) {
		Date date = new Date();

		return Jwts.builder()
			.setSubject(userId) // 정보 저장
			.setIssuedAt(date) // 토큰 발행 시간 정보
			.setExpiration(new Date(date.getTime() + tokenValid)) // set Expire Time
			.signWith(key, signatureAlgorithm)  // 사용할 암호화 알고리즘과
			.compact();
	}

	// header 에서 AccessToken 가져오기
	public String resolveAccessToken(HttpServletRequest request) {
		if (request.getHeader("Authorization") != null)
			return request.getHeader("Authorization").substring(7);
		return null;
	}

	// header 에서 RefreshToken 가져오기
	public String resolveRefreshToken(HttpServletRequest request) {
		if (request.getHeader("RefreshToken") != null)
			return request.getHeader("RefreshToken").substring(7);
		return null;
	}


	// 토큰 검증
	public boolean validateToken(String token, Key key) {
		try {
			//Jwts: 위에서 JWT 생성 시 사용했던 것
			//parserBuilder(): 검증 방법
			//setSigningKey(key): Token 을 만들 때 사용한 KEY
			//parseClaimsJws(token): 어떤 Token 을 검증할 것인지
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException e) {
			log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		} catch (ExpiredJwtException e) {
			log.info("Expired JWT token, 만료된 JWT token 입니다.");
			return true;
		}
		return false;
	}

	// 어세스 토큰 헤더 설정
	public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
		response.setHeader("Authorization", "bearer " + accessToken);
	}

	// 리프레시 토큰 헤더 설정
	public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
		response.setHeader("RefreshToken", "bearer " + refreshToken);
	}

	// RefreshToken 존재유무 확인
	public boolean existsRefreshToken(String refreshToken) {
		return redisService.getValues(refreshToken) != null;
	}

	public Claims getUserInfoFromToken(Key key, String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Authentication createAuthentication(String userId) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

	public boolean isExpired(Key key, String token) {
		try {
			// accessToken 남은 유효시간
			Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration();
			// 현재 시간
			Long now = new Date().getTime();
			return (expiration.getTime() - now) > 0 ? false : true;
		} catch (ExpiredJwtException e) {
			return true;
		}

	}
}
