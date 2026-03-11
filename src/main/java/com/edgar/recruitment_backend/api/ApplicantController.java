package com.edgar.recruitment_backend.api;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edgar.recruitment_backend.api.dto.ApplicationDtos;
import com.edgar.recruitment_backend.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applicant/applications")
@Validated
public class ApplicantController {

	private final ApplicationService applicationService;

	public ApplicantController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Long> submit(Authentication authentication,
			@Valid ApplicationDtos.ApplicantSubmitRequest request,
			@RequestParam("cv") MultipartFile cvFile) throws IOException {
		Long id = applicationService.submit(authentication, request, cvFile);
		return ResponseEntity.ok(id);
	}

	@GetMapping
	public List<ApplicationDtos.ApplicationSummary> list(Authentication authentication) {
		return applicationService.listForApplicant(authentication);
	}

	@GetMapping("/{id}")
	public ApplicationDtos.ApplicationDetail get(Authentication authentication, @PathVariable Long id) {
		return applicationService.getDetailsForApplicant(authentication, id);
	}

	@GetMapping("/{id}/cv")
	public ResponseEntity<byte[]> downloadCv(Authentication authentication, @PathVariable Long id) throws IOException {
		byte[] data = applicationService.loadApplicantCv(authentication, id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cv.pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(data);
	}
}

