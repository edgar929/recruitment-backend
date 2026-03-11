package com.edgar.recruitment_backend.api.dto;

import jakarta.validation.constraints.NotBlank;

public class AuthDtos {

	public record LoginRequest(
			@NotBlank String username,
			@NotBlank String password
	) {
	}

	public record RegisterApplicantRequest(
			@NotBlank String username,
			@NotBlank String password
	) {
	}

	public record AuthResponse(
			String token
	) {
	}
}

