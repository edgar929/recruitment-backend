package com.edgar.recruitment_backend.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edgar.recruitment_backend.api.dto.AuthDtos;
import com.edgar.recruitment_backend.api.dto.ErrorResponse;
import com.edgar.recruitment_backend.domain.AppUser;
import com.edgar.recruitment_backend.domain.Role;
import com.edgar.recruitment_backend.repo.AppUserRepository;
import com.edgar.recruitment_backend.security.JwtService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final AppUserRepository appUserRepository;
	private final PasswordEncoder passwordEncoder;

	public AuthController(
			AuthenticationManager authenticationManager,
			JwtService jwtService,
			AppUserRepository appUserRepository,
			PasswordEncoder passwordEncoder) {
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.appUserRepository = appUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@PostMapping("/login")
	public ResponseEntity<AuthDtos.AuthResponse> login(@Valid @RequestBody AuthDtos.LoginRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.username(), request.password()));

		String username = authentication.getName();
		var user = appUserRepository.findByUsername(username).orElseThrow();
		String token = jwtService.generateToken(username, user.getRole().name());
		return ResponseEntity.ok(new AuthDtos.AuthResponse(token));
	}

	@PostMapping("/register")
	@Transactional
	public ResponseEntity<?> register(@Valid @RequestBody AuthDtos.RegisterApplicantRequest request) {
		if (appUserRepository.existsByUsername(request.username())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(ErrorResponse.of("username_exists", "Username already taken. Please choose another."));
		}
		AppUser user = new AppUser();
		user.setUsername(request.username());
		user.setPasswordHash(passwordEncoder.encode(request.password()));
		user.setRole(Role.APPLICANT);
		user.setEnabled(true);
		appUserRepository.save(user);

		String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
		return ResponseEntity.ok(new AuthDtos.AuthResponse(token));
	}
}

