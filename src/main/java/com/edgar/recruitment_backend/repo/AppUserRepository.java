package com.edgar.recruitment_backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edgar.recruitment_backend.domain.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	Optional<AppUser> findByUsername(String username);
	boolean existsByUsername(String username);
}

