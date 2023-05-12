package com.example.hanghaetinder_bemain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class HanghaeTinderBeMainApplication {

	public static void main(String[] args) {
		SpringApplication.run(HanghaeTinderBeMainApplication.class, args);
	}

}
