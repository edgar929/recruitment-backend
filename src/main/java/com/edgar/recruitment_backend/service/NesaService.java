package com.edgar.recruitment_backend.service;

import org.springframework.stereotype.Service;

@Service
public class NesaService {

	private static final String[] GRADES = { "A", "B", "C", "A+", "B+", "A-", "B-" };

	private static final String[] OPTIONS = {
			"Computer Science", "Information Systems", "Software Engineering",
			"Data Science", "Cybersecurity", "Electrical Engineering",
			"Mathematics", "Physics", "Business IT", "Network Engineering"
	};

	public record NesaRecord(
			String candidateId,
			String grade,
			String optionAttended
	) {
	}

	/**
	 * Simulated NESA lookup: returns a deterministic record per candidateId
	 * so different applicants get different grades and options.
	 */
	public NesaRecord fetchRecord(String candidateId) {
		int hash = (candidateId != null ? candidateId.hashCode() : 0) & 0x7FFF_FFFF;
		int gradeIndex = hash % GRADES.length;
		int optionIndex = (hash / GRADES.length) % OPTIONS.length;
		return new NesaRecord(
				candidateId != null ? candidateId : "",
				GRADES[gradeIndex],
				OPTIONS[optionIndex]
		);
	}
}

