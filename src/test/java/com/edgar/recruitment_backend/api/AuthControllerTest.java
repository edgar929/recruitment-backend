package com.edgar.recruitment_backend.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.edgar.recruitment_backend.repo.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private AppUserRepository appUserRepository;

	@Test
	void register_withNewUsername_returns200AndToken() throws Exception {
		String username = "newapp_" + System.currentTimeMillis();
		String body = objectMapper.writeValueAsString(
				java.util.Map.of("username", username, "password", "pass123"));

		mockMvc.perform(post("/api/auth/register")
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isString());
	}

	@Test
	void register_withExistingUsername_returns409AndMessage() throws Exception {
		String username = "dupuser_" + System.currentTimeMillis();
		// Create user first
		var user = new com.edgar.recruitment_backend.domain.AppUser();
		user.setUsername(username);
		user.setPasswordHash("hash");
		user.setRole(com.edgar.recruitment_backend.domain.Role.APPLICANT);
		user.setEnabled(true);
		appUserRepository.save(user);

		String body = objectMapper.writeValueAsString(
				java.util.Map.of("username", username, "password", "pass123"));

		mockMvc.perform(post("/api/auth/register")
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error").value("username_exists"))
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void login_withValidCredentials_returns200AndToken() throws Exception {
		String username = "loginuser_" + System.currentTimeMillis();
		var user = new com.edgar.recruitment_backend.domain.AppUser();
		user.setUsername(username);
		user.setPasswordHash("$2a$10$XQPnR8jT5YzGJVLZqGqQeOQZqGqQeOQZqGqQeOQZqGqQeOQZqGqQe"); // BCrypt for "password"
		user.setRole(com.edgar.recruitment_backend.domain.Role.APPLICANT);
		user.setEnabled(true);
		appUserRepository.save(user);

		// We need the actual encoded password - use a known one. Spring Security test has encoding.
		org.springframework.security.crypto.password.PasswordEncoder encoder =
				new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
		user.setPasswordHash(encoder.encode("password"));
		appUserRepository.save(user);

		String body = objectMapper.writeValueAsString(
				java.util.Map.of("username", username, "password", "password"));

		mockMvc.perform(post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").isString());
	}

	@Test
	void login_withInvalidCredentials_returns401AndMessage() throws Exception {
		String body = objectMapper.writeValueAsString(
				java.util.Map.of("username", "nonexistent", "password", "wrong"));

		mockMvc.perform(post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error").value("invalid_credentials"))
				.andExpect(jsonPath("$.message").exists());
	}
}
