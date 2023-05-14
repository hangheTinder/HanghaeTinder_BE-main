package com.example.hanghaetinder_bemain.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.hanghaetinder_bemain.jwt.JwtUtil;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@OpenAPIDefinition(
	info = @Info(title = "Tinder API 명세서",
		description = "Tinder API 명세서",
		version = "v1"))

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI openAPI() {
		io.swagger.v3.oas.models.info.Info info = new io.swagger.v3.oas.models.info.Info()
			.version("v1.0.0")
			.title("Tinder_clone")
			.description("Api Description");

		String access_token_header = JwtUtil.AUTHORIZATION_HEADER;

		// 헤더에 security scheme 도 같이 보내게 만드는 것
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(access_token_header);

		Components components = new Components()
			.addSecuritySchemes(access_token_header, new SecurityScheme()
				.name(access_token_header)
				.type(SecurityScheme.Type.APIKEY)
				.in(SecurityScheme.In.HEADER));


		return new OpenAPI()
			.info(info)
			.addSecurityItem(securityRequirement)
			.components(components);
	}

}