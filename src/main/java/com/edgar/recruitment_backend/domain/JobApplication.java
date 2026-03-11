package com.edgar.recruitment_backend.domain;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_application")
@Getter
@Setter
@NoArgsConstructor
public class JobApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "applicant_user_id", nullable = false)
	private AppUser applicant;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private ApplicationStatus status = ApplicationStatus.SUBMITTED;

	@Column(name = "decision_reason", length = 500)
	private String decisionReason;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewed_by_user_id")
	private AppUser reviewedBy;

	@Column(name = "reviewed_at")
	private Instant reviewedAt;

	@Column(name = "nid_number", nullable = false, length = 50)
	private String nidNumber;

	@Column(name = "nid_first_name", nullable = false, length = 100)
	private String nidFirstName;

	@Column(name = "nid_last_name", nullable = false, length = 100)
	private String nidLastName;

	@Column(name = "nid_date_of_birth", nullable = false)
	private LocalDate nidDateOfBirth;

	@Column(name = "nesa_candidate_id", nullable = false, length = 50)
	private String nesaCandidateId;

	@Column(name = "nesa_grade", nullable = false, length = 50)
	private String nesaGrade;

	@Column(name = "nesa_option_attended", nullable = false, length = 100)
	private String nesaOptionAttended;

	@Column(name = "cv_original_filename", nullable = false, length = 255)
	private String cvOriginalFilename;

	@Column(name = "cv_content_type", nullable = false, length = 100)
	private String cvContentType;

	@Column(name = "cv_size_bytes", nullable = false)
	private long cvSizeBytes;

	@Column(name = "cv_storage_path", nullable = false, length = 500)
	private String cvStoragePath;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@PrePersist
	void prePersist() {
		var now = Instant.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	void preUpdate() {
		this.updatedAt = Instant.now();
	}
}

