package com.edgar.recruitment_backend.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

	@Autowired
	private JwtService jwtService;

	@Test
	void generateToken_andExtractUsername_roundTrip() {
		String token = jwtService.generateToken("testuser", "APPLICANT");
		assertThat(token).isNotBlank();
		assertThat(jwtService.extractUsername(token)).isEqualTo("testuser");
	}

	@Test
	void extractRole_returnsRoleFromToken() {
		String token = jwtService.generateToken("hruser", "HR");
		assertThat(jwtService.extractRole(token)).isEqualTo("HR");
	}

	@Test
	void differentUsers_produceDifferentTokens() {
		String t1 = jwtService.generateToken("user1", "APPLICANT");
		String t2 = jwtService.generateToken("user2", "APPLICANT");
		assertThat(t1).isNotEqualTo(t2);
		assertThat(jwtService.extractUsername(t1)).isEqualTo("user1");
		assertThat(jwtService.extractUsername(t2)).isEqualTo("user2");
	}
}
