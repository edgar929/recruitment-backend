package com.edgar.recruitment_backend.api.dto;

/**
 * Consistent error response body for API clients.
 */
public record ErrorResponse(String error, String message) {

	public static ErrorResponse of(String error, String message) {
		return new ErrorResponse(error, message);
	}
}
