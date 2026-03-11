package com.edgar.recruitment_backend.api.dto;

import jakarta.validation.constraints.NotBlank;

public class HrDtos {

	public record ReviewRequest(
			boolean approve,
			@NotBlank String reason
	) {
	}
}

