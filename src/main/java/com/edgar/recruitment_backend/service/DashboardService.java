package com.edgar.recruitment_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edgar.recruitment_backend.api.dto.DashboardDtos;
import com.edgar.recruitment_backend.domain.ApplicationStatus;
import com.edgar.recruitment_backend.repo.JobApplicationRepository;

@Service
public class DashboardService {

	private final JobApplicationRepository jobApplicationRepository;

	public DashboardService(JobApplicationRepository jobApplicationRepository) {
		this.jobApplicationRepository = jobApplicationRepository;
	}

	@Transactional(readOnly = true)
	public DashboardDtos.Summary getSummary() {
		long total = jobApplicationRepository.count();
		long submitted = jobApplicationRepository.countByStatus(ApplicationStatus.SUBMITTED);
		long approved = jobApplicationRepository.countByStatus(ApplicationStatus.APPROVED);
		long rejected = jobApplicationRepository.countByStatus(ApplicationStatus.REJECTED);
		return new DashboardDtos.Summary(total, submitted, approved, rejected);
	}
}

