package com.edgar.recruitment_backend.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(
		Jwt jwt,
		Storage storage
) {
	public record Jwt(
			String issuer,
			String secret,
			Duration accessTokenTtl
	) {
	}

	public record Storage(
			String cvDir
	) {
	}
}

