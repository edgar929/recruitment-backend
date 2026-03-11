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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.recruitment_backend.api.dto.ApplicationDtos;
import com.edgar.recruitment_backend.api.dto.HrDtos;
import com.edgar.recruitment_backend.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/hr/applications")
@Validated
public class HrController {

	private final ApplicationService applicationService;

	public HrController(ApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	@GetMapping
	public List<ApplicationDtos.ApplicationSummary> listLatestSorted() {
		return applicationService.listForHrLatest10();
	}

	@GetMapping("/{id}")
	public ApplicationDtos.ApplicationDetail get(@PathVariable Long id) {
		return applicationService.getDetailsForHr(id);
	}

	@PostMapping("/{id}/review")
	public ResponseEntity<Void> review(@PathVariable Long id,
			@Valid @RequestBody HrDtos.ReviewRequest request,
			Authentication authentication) {
		applicationService.review(id, request.approve(), request.reason(), authentication);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/cv")
	public ResponseEntity<byte[]> downloadCv(@PathVariable Long id) throws IOException {
		byte[] data = applicationService.loadCvForHr(id);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cv.pdf\"")
				.contentType(MediaType.APPLICATION_PDF)
				.body(data);
	}
}

