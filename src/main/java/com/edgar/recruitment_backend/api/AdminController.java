package com.edgar.recruitment_backend.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.recruitment_backend.api.dto.AdminDtos;
import com.edgar.recruitment_backend.api.dto.ErrorResponse;
import com.edgar.recruitment_backend.domain.AppUser;
import com.edgar.recruitment_backend.repo.AppUserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/users")
@Validated
public class AdminController {

	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminController(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping
	public List<AdminDtos.UserSummary> list() {
		return appUserRepository.findAll().stream()
				.map(u -> new AdminDtos.UserSummary(u.getId(), u.getUsername(), u.getRole(), u.isEnabled()))
				.toList();
	}

	@PostMapping
	public ResponseEntity<?> create(@Valid @RequestBody AdminDtos.CreateUserRequest request) {
		if (appUserRepository.existsByUsername(request.username())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(ErrorResponse.of("username_exists", "Username already exists. Choose another."));
		}
		AppUser user = new AppUser();
		user.setUsername(request.username());
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		user.setRole(request.role());
		user.setEnabled(true);
		appUserRepository.save(user);
		return ResponseEntity.ok(new AdminDtos.UserSummary(user.getId(), user.getUsername(), user.getRole(),
				user.isEnabled()));
	}

	@PutMapping("/{id}/status")
	public ResponseEntity<Void> updateStatus(@PathVariable Long id,
			@Valid @RequestBody AdminDtos.UpdateUserStatusRequest request) {
		var user = appUserRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		user.setEnabled(request.enabled());
		appUserRepository.save(user);
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}/reset-password")
	public ResponseEntity<Void> resetPassword(@PathVariable Long id,
			@Valid @RequestBody AdminDtos.ResetPasswordRequest request) {
		var user = appUserRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
		appUserRepository.save(user);
		return ResponseEntity.noContent().build();
	}
}

