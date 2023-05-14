package com.example.hanghaetinder_bemain.sevice;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

import com.example.hanghaetinder_bemain.dto.http.ResponseMessage;
import com.example.hanghaetinder_bemain.exception.CustomException;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate redisTemplate;

	public void setValues(String token, String userId) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(token, userId, Duration.ofMinutes(30L));
	}

	public String getValues(String token) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return values.get(token);
	}

	public void deleteValues(String token) {
		System.out.println(token);
		if(redisTemplate.delete(token)) {
			return;
		} else {
			throw new CustomException(ResponseMessage.LOGOUT_FAIL);
		}
	}
}
