package com.edgar.recruitment_backend.api.dto;

import java.time.Instant;

import com.edgar.recruitment_backend.domain.ApplicationStatus;

import jakarta.validation.constraints.NotBlank;

public class ApplicationDtos {

	public record ApplicantSubmitRequest(
			@NotBlank String nidNumber,
			@NotBlank String nesaCandidateId
	) {
	}

	public record ApplicationSummary(
			Long id,
			ApplicationStatus status,
			String decisionReason,
			String fullName,
			Instant createdAt
	) {
	}

	public record ApplicationDetail(
			Long id,
			ApplicationStatus status,
			String decisionReason,
			String nidNumber,
			String firstName,
			String lastName,
			String nesaCandidateId,
			String nesaGrade,
			String nesaOptionAttended,
			Instant createdAt
	) {
	}
}

