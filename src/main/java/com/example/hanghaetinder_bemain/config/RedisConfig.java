
package com.example.hanghaetinder_bemain.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.hanghaetinder_bemain.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching
public class RedisConfig {

	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}
//채팅 메세지 템플릿
	@Bean
	public RedisTemplate<String, ChatMessage> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, ChatMessage> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// JSR310 모듈을 추가합니다.
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
		template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessage.class));
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setKeySerializer(new StringRedisSerializer());
		template.afterPropertiesSet();
		return template;
	}

	// 로그아웃 템플릿
	@Bean
	public RedisTemplate<String, String> user_redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		// string을 기반으로 하는 레디스 템플릿 만들기
		RedisTemplate<String, String> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);

		// 문자열과 키의 값들을 직렬화 한다.
		template.setKeySerializer(new GenericToStringSerializer<>(String.class));
		template.setValueSerializer(new GenericToStringSerializer<>(String.class));


		return template;
	}

}