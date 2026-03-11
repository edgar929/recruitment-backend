package com.edgar.recruitment_backend.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;

import com.edgar.recruitment_backend.api.dto.ErrorResponse;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void handleBadCredentials_returns401WithMessage() {
		ResponseEntity<ErrorResponse> res = handler.handleBadCredentials(new BadCredentialsException("bad"));
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().error()).isEqualTo("invalid_credentials");
		assertThat(res.getBody().message()).isNotBlank();
	}

	@Test
	void handleDisabled_returns403WithMessage() {
		ResponseEntity<ErrorResponse> res = handler.handleDisabled(new DisabledException("disabled"));
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().error()).isEqualTo("account_disabled");
		assertThat(res.getBody().message()).isNotBlank();
	}

	@Test
	void handleNotFound_returns404WithMessage() {
		ResponseEntity<ErrorResponse> res = handler.handleNotFound(
				new ResourceNotFoundException("User not found with id: 42"));
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().error()).isEqualTo("not_found");
		assertThat(res.getBody().message()).isEqualTo("User not found with id: 42");
	}

	@Test
	void handleIllegalArgument_returns400WithMessage() {
		ResponseEntity<ErrorResponse> res = handler.handleIllegalArgument(
				new IllegalArgumentException("Not owner of application"));
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().error()).isEqualTo("bad_request");
		assertThat(res.getBody().message()).isEqualTo("Not owner of application");
	}

	@Test
	void handleGeneric_returns500WithSafeMessage() {
		ResponseEntity<ErrorResponse> res = handler.handleGeneric(new RuntimeException("internal"));
		assertThat(res.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(res.getBody()).isNotNull();
		assertThat(res.getBody().error()).isEqualTo("internal_error");
		assertThat(res.getBody().message()).doesNotContain("internal");
	}
}
