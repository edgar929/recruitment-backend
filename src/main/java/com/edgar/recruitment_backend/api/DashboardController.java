package com.edgar.recruitment_backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.recruitment_backend.api.dto.DashboardDtos;
import com.edgar.recruitment_backend.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	private final DashboardService dashboardService;

	public DashboardController(DashboardService dashboardService) {
		this.dashboardService = dashboardService;
	}

	@GetMapping("/summary")
	public DashboardDtos.Summary getSummary() {
		return dashboardService.getSummary();
	}
}

