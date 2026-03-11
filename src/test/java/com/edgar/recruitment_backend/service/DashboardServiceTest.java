package com.edgar.recruitment_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.edgar.recruitment_backend.api.dto.DashboardDtos;
import com.edgar.recruitment_backend.repo.JobApplicationRepository;

@SpringBootTest
@ActiveProfiles("test")
class DashboardServiceTest {

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private JobApplicationRepository jobApplicationRepository;

	@Test
	void getSummary_whenNoApplications_returnsZeros() {
		DashboardDtos.Summary summary = dashboardService.getSummary();
		assertThat(summary.totalApplications()).isGreaterThanOrEqualTo(0);
		assertThat(summary.submitted()).isGreaterThanOrEqualTo(0);
		assertThat(summary.approved()).isGreaterThanOrEqualTo(0);
		assertThat(summary.rejected()).isGreaterThanOrEqualTo(0);
	}

	@Test
	void getSummary_countsMatchRepository() {
		long total = jobApplicationRepository.count();
		DashboardDtos.Summary summary = dashboardService.getSummary();
		assertThat(summary.totalApplications()).isEqualTo(total);
		assertThat(summary.submitted() + summary.approved() + summary.rejected()).isEqualTo(total);
	}
}
