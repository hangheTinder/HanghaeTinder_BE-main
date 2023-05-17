package com.example.hanghaetinder_bemain.domain.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10); // 적절한 스레드 풀 크기를 설정합니다.
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("AsyncTask-");
		executor.initialize();
		return executor;
	}

}