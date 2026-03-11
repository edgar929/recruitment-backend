package com.edgar.recruitment_backend.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class NidService {

	private static final String[] FIRST_NAMES = {
			"Alice", "Bob", "Carol", "David", "Emma", "Frank", "Grace", "Henry",
			"Ivy", "James", "Kate", "Leo", "Mia", "Noah", "Olivia", "Paul",
			"Quinn", "Ryan", "Sara", "Tom", "Uma", "Victor", "Wendy", "Xavier",
			"Yara", "Zane", "Anna", "Ben", "Clara", "Daniel"
	};

	private static final String[] LAST_NAMES = {
			"Habimana", "Niyonsenga", "Uwimana", "Mukiza", "Nsengimana", "Kayitesi", "Murekatete", "Ndahiro",
			"Iradukunda", "Uwase", "Mugabe", "Ndayisaba", "Uwiringiyimana", "Niyonzima", "Mugisha", "Niyibizi",
			"Hirwa", "Murebwayire", "Niyomugabo", "Umutoni", "Mutesi", "Niyonsaba", "Habiyambere", "Nkurunziza",
			"Rukundo", "Mugwaneza", "Niyigena", "Uwambajimana", "Nshimiyimana", "Mukundwa"
	};

	public record NidProfile(
			String nidNumber,
			String firstName,
			String lastName,
			LocalDate dateOfBirth
	) {
	}

	/**
	 * Simulated NID lookup: returns a deterministic profile per nidNumber
	 * so different applicants get different names.
	 */
	public NidProfile fetchProfile(String nidNumber) {
		int hash = (nidNumber != null ? nidNumber.hashCode() : 0) & 0x7FFF_FFFF;
		int firstIndex = hash % FIRST_NAMES.length;
		int lastIndex = (hash / FIRST_NAMES.length) % LAST_NAMES.length;
		// Vary DOB by hash (e.g. 1980–2000)
		int year = 1980 + (hash % 21);
		int month = 1 + (hash % 12);
		int day = 1 + (hash % 28);
		return new NidProfile(
				nidNumber != null ? nidNumber : "",
				FIRST_NAMES[firstIndex],
				LAST_NAMES[lastIndex],
				LocalDate.of(year, month, day)
		);
	}
}

