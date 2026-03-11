package com.edgar.recruitment_backend.api.dto;

public class DashboardDtos {

	public record Summary(
			long totalApplications,
			long submitted,
			long approved,
			long rejected
	) {
	}
}

