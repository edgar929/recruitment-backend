package com.edgar.recruitment_backend.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.edgar.recruitment_backend.api.dto.ApplicationDtos;
import com.edgar.recruitment_backend.domain.ApplicationStatus;
import com.edgar.recruitment_backend.domain.JobApplication;
import com.edgar.recruitment_backend.repo.AppUserRepository;
import com.edgar.recruitment_backend.repo.JobApplicationRepository;

@Service
public class ApplicationService {

	private final JobApplicationRepository jobApplicationRepository;
	private final AppUserRepository appUserRepository;
	private final NidService nidService;
	private final NesaService nesaService;
	private final CvStorageService cvStorageService;

	public ApplicationService(JobApplicationRepository jobApplicationRepository,
			AppUserRepository appUserRepository,
			NidService nidService,
			NesaService nesaService,
			CvStorageService cvStorageService) {
		this.jobApplicationRepository = jobApplicationRepository;
		this.appUserRepository = appUserRepository;
		this.nidService = nidService;
		this.nesaService = nesaService;
		this.cvStorageService = cvStorageService;
	}

	@Transactional
	public Long submit(Authentication authentication,
			ApplicationDtos.ApplicantSubmitRequest request,
			MultipartFile cvFile) throws IOException {

		var user = appUserRepository.findByUsername(authentication.getName()).orElseThrow();

		var nid = nidService.fetchProfile(request.nidNumber());
		var nesa = nesaService.fetchRecord(request.nesaCandidateId());

		String storagePath = cvStorageService.store(cvFile);

		JobApplication app = new JobApplication();
		app.setApplicant(user);
		app.setStatus(ApplicationStatus.SUBMITTED);
		app.setNidNumber(nid.nidNumber());
		app.setNidFirstName(nid.firstName());
		app.setNidLastName(nid.lastName());
		app.setNidDateOfBirth(nid.dateOfBirth());
		app.setNesaCandidateId(nesa.candidateId());
		app.setNesaGrade(nesa.grade());
		app.setNesaOptionAttended(nesa.optionAttended());
		app.setCvOriginalFilename(cvFile.getOriginalFilename());
		app.setCvContentType(cvFile.getContentType() != null ? cvFile.getContentType() : "application/octet-stream");
		app.setCvSizeBytes(cvFile.getSize());
		app.setCvStoragePath(storagePath);

		jobApplicationRepository.save(app);

		return app.getId();
	}

	@Transactional(readOnly = true)
	public List<ApplicationDtos.ApplicationSummary> listForApplicant(Authentication authentication) {
		var list = jobApplicationRepository.findByApplicantUsername(authentication.getName(), PageRequest.of(0, 20));
		return list.stream()
				.map(a -> new ApplicationDtos.ApplicationSummary(
						a.getId(),
						a.getStatus(),
						a.getDecisionReason(),
						a.getNidFirstName() + " " + a.getNidLastName(),
						a.getCreatedAt()))
				.toList();
	}

	@Transactional(readOnly = true)
	public List<ApplicationDtos.ApplicationSummary> listForHrLatest10() {
		var latest = jobApplicationRepository.findLatestWithApplicant(PageRequest.of(0, 10));
		return latest.stream()
				.sorted(Comparator.comparing(JobApplication::getNidLastName)
						.thenComparing(JobApplication::getNidFirstName))
				.map(a -> new ApplicationDtos.ApplicationSummary(
						a.getId(),
						a.getStatus(),
						a.getDecisionReason(),
						a.getNidFirstName() + " " + a.getNidLastName(),
						a.getCreatedAt()))
				.toList();
	}

	@Transactional(readOnly = true)
	public ApplicationDtos.ApplicationDetail getDetailsForHr(Long id) {
		var app = jobApplicationRepository.findById(id).orElseThrow();
		return toDetail(app);
	}

	@Transactional
	public void review(Long id, boolean approve, String reason, Authentication reviewerAuth) {
		var app = jobApplicationRepository.findById(id).orElseThrow();
		var reviewer = appUserRepository.findByUsername(reviewerAuth.getName()).orElseThrow();
		app.setStatus(approve ? ApplicationStatus.APPROVED : ApplicationStatus.REJECTED);
		app.setDecisionReason(reason);
		app.setReviewedBy(reviewer);
		app.setReviewedAt(Instant.now());
	}

	@Transactional(readOnly = true)
	public byte[] loadCvForHr(Long id) throws IOException {
		var app = jobApplicationRepository.findById(id).orElseThrow();
		return cvStorageService.load(app.getCvStoragePath());
	}

	@Transactional(readOnly = true)
	public byte[] loadApplicantCv(Authentication authentication, Long id) throws IOException {
		var app = jobApplicationRepository.findById(id).orElseThrow();
		if (!app.getApplicant().getUsername().equals(authentication.getName())) {
			throw new IllegalArgumentException("Not owner of application");
		}
		return cvStorageService.load(app.getCvStoragePath());
	}

	@Transactional(readOnly = true)
	public ApplicationDtos.ApplicationDetail getDetailsForApplicant(Authentication authentication, Long id) {
		var app = jobApplicationRepository.findById(id).orElseThrow();
		if (!app.getApplicant().getUsername().equals(authentication.getName())) {
			throw new IllegalArgumentException("Not owner of application");
		}
		return toDetail(app);
	}

	private ApplicationDtos.ApplicationDetail toDetail(JobApplication a) {
		return new ApplicationDtos.ApplicationDetail(
				a.getId(),
				a.getStatus(),
				a.getDecisionReason(),
				a.getNidNumber(),
				a.getNidFirstName(),
				a.getNidLastName(),
				a.getNesaCandidateId(),
				a.getNesaGrade(),
				a.getNesaOptionAttended(),
				a.getCreatedAt() != null ? a.getCreatedAt() : Instant.now());
	}
}

