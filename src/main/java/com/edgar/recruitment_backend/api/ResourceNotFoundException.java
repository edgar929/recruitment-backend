package com.edgar.recruitment_backend.api;

/**
 * Thrown when a requested resource (e.g. user, application) does not exist.
 * Mapped to 404 with a message in GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
