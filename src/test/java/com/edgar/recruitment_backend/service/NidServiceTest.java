package com.edgar.recruitment_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NidServiceTest {

	private final NidService nidService = new NidService();

	@Test
	void fetchProfile_returnsDeterministicProfileForSameNid() {
		var p1 = nidService.fetchProfile("1234567890123");
		var p2 = nidService.fetchProfile("1234567890123");
		assertThat(p1.nidNumber()).isEqualTo("1234567890123");
		assertThat(p1.firstName()).isEqualTo(p2.firstName());
		assertThat(p1.lastName()).isEqualTo(p2.lastName());
		assertThat(p1.dateOfBirth()).isEqualTo(p2.dateOfBirth());
	}

	@Test
	void fetchProfile_returnsDifferentProfilesForDifferentNids() {
		var p1 = nidService.fetchProfile("111");
		var p2 = nidService.fetchProfile("222");
		boolean different = !p1.firstName().equals(p2.firstName()) || !p1.lastName().equals(p2.lastName());
		assertThat(different).isTrue();
	}

	@Test
	void fetchProfile_returnsRwandanLastNameNotWestern() {
		var profile = nidService.fetchProfile("any");
		assertThat(profile.lastName()).isNotBlank();
		// Ensure we are not returning old Western surnames
		assertThat(profile.lastName()).isNotIn("Smith", "Johnson", "Williams", "Brown", "Jones");
	}

	@Test
	void fetchProfile_handlesNullNidNumber() {
		var profile = nidService.fetchProfile(null);
		assertThat(profile.nidNumber()).isEmpty();
		assertThat(profile.firstName()).isNotBlank();
		assertThat(profile.lastName()).isNotBlank();
		assertThat(profile.dateOfBirth()).isNotNull();
	}
}
