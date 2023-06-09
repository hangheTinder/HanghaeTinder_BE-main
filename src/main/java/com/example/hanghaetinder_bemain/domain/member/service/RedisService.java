package com.example.hanghaetinder_bemain.domain.member.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.example.hanghaetinder_bemain.domain.common.dto.ResponseMessage;
import com.example.hanghaetinder_bemain.domain.common.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate redisTemplate;

	public void setValues(final String token, final String userId) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		values.set(token, userId, Duration.ofMinutes(30L));
	}

	public String getValues(final String token) {
		ValueOperations<String, String> values = redisTemplate.opsForValue();
		return values.get(token);
	}

	public void deleteValues(final String token) {
		System.out.println(token);
		if(redisTemplate.delete(token)) {
			return;
		} else {
			throw new CustomException(ResponseMessage.LOGOUT_FAIL);
		}
	}
}
