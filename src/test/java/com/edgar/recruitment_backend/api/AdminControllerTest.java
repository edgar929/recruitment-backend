package com.edgar.recruitment_backend.api;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.edgar.recruitment_backend.repo.AppUserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private AppUserRepository appUserRepository;

	private String loginAsAdmin() throws Exception {
		String body = objectMapper.writeValueAsString(
				java.util.Map.of("username", "admin", "password", "admin123!"));
		ResultActions result = mockMvc.perform(post("/api/auth/login")
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk());
		String response = result.andReturn().getResponse().getContentAsString();
		return objectMapper.readTree(response).get("token").asText();
	}

	@Test
	void listUsers_withoutAuth_returns4xx() throws Exception {
		// No token: Spring Security returns 401 or 403 depending on configuration
		mockMvc.perform(get("/api/admin/users"))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void listUsers_asAdmin_returns200AndList() throws Exception {
		String token = loginAsAdmin();
		mockMvc.perform(get("/api/admin/users")
				.header("Authorization", "Bearer " + token))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	@Test
	void createUser_withNewUsername_returns200AndUserSummary() throws Exception {
		String token = loginAsAdmin();
		String username = "newhr_" + System.currentTimeMillis();
		String body = objectMapper.writeValueAsString(java.util.Map.of(
				"username", username,
				"password", "pass123!",
				"role", "HR"));

		mockMvc.perform(post("/api/admin/users")
				.header("Authorization", "Bearer " + token)
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value(username))
				.andExpect(jsonPath("$.role").value("HR"))
				.andExpect(jsonPath("$.enabled").value(true));
	}

	@Test
	void createUser_withExistingUsername_returns409AndMessage() throws Exception {
		String token = loginAsAdmin();
		String username = "dup_" + System.currentTimeMillis();
		var user = new com.edgar.recruitment_backend.domain.AppUser();
		user.setUsername(username);
		user.setPasswordHash("hash");
		user.setRole(com.edgar.recruitment_backend.domain.Role.APPLICANT);
		user.setEnabled(true);
		appUserRepository.save(user);

		String body = objectMapper.writeValueAsString(java.util.Map.of(
				"username", username,
				"password", "pass123!",
				"role", "HR"));

		mockMvc.perform(post("/api/admin/users")
				.header("Authorization", "Bearer " + token)
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error").value("username_exists"))
				.andExpect(jsonPath("$.message").exists());
	}

	@Test
	void updateStatus_withInvalidId_returns404() throws Exception {
		String token = loginAsAdmin();
		String body = objectMapper.writeValueAsString(java.util.Map.of("enabled", false));

		mockMvc.perform(put("/api/admin/users/999999/status")
				.header("Authorization", "Bearer " + token)
				.contentType(APPLICATION_JSON)
				.content(body))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error").value("not_found"));
	}
}
