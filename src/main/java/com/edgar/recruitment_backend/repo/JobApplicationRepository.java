package com.edgar.recruitment_backend.repo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.edgar.recruitment_backend.domain.ApplicationStatus;
import com.edgar.recruitment_backend.domain.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

	@Query("""
		select ja
		from JobApplication ja
		join fetch ja.applicant a
		order by ja.createdAt desc
		""")
	List<JobApplication> findLatestWithApplicant(Pageable pageable);

	@Query("""
		select ja
		from JobApplication ja
		join fetch ja.applicant a
		where a.username = :username
		order by ja.createdAt desc
		""")
	List<JobApplication> findByApplicantUsername(@Param("username") String username, Pageable pageable);

	long countByStatus(ApplicationStatus status);
}

