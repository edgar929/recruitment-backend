package com.edgar.recruitment_backend.api.dto;

import com.edgar.recruitment_backend.domain.Role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AdminDtos {

	public record UserSummary(
			Long id,
			String username,
			Role role,
			boolean enabled
	) {
	}

	public record CreateUserRequest(
			@NotBlank String username,
			@NotBlank String password,
			@NotNull Role role
	) {
	}

	public record UpdateUserStatusRequest(
			boolean enabled
	) {
	}

	public record ResetPasswordRequest(
			@NotBlank String newPassword
	) {
	}
}

