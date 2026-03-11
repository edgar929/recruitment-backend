package com.edgar.recruitment_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NesaServiceTest {

	private final NesaService nesaService = new NesaService();

	@Test
	void fetchRecord_returnsDeterministicRecordForSameCandidateId() {
		var r1 = nesaService.fetchRecord("CAND001");
		var r2 = nesaService.fetchRecord("CAND001");
		assertThat(r1.candidateId()).isEqualTo("CAND001");
		assertThat(r1.grade()).isEqualTo(r2.grade());
		assertThat(r1.optionAttended()).isEqualTo(r2.optionAttended());
	}

	@Test
	void fetchRecord_returnsDifferentRecordsForDifferentIds() {
		var r1 = nesaService.fetchRecord("CAND001");
		var r2 = nesaService.fetchRecord("CAND002");
		boolean different = !r1.grade().equals(r2.grade()) || !r1.optionAttended().equals(r2.optionAttended());
		assertThat(different).isTrue();
	}

	@Test
	void fetchRecord_returnsValidGradeAndOption() {
		var record = nesaService.fetchRecord("any");
		assertThat(record.grade()).isNotBlank();
		assertThat(record.optionAttended()).isNotBlank();
	}

	@Test
	void fetchRecord_handlesNullCandidateId() {
		var record = nesaService.fetchRecord(null);
		assertThat(record.candidateId()).isEmpty();
		assertThat(record.grade()).isNotBlank();
		assertThat(record.optionAttended()).isNotBlank();
	}
}
