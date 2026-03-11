package com.edgar.recruitment_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.edgar.recruitment_backend.domain.AppUser;
import com.edgar.recruitment_backend.domain.Role;
import com.edgar.recruitment_backend.repo.AppUserRepository;

@Configuration
public class DataBootstrap {

	@Bean
	CommandLineRunner seedDefaultUsers(AppUserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			try {
				if (userRepository.count() == 0) {
					AppUser admin = new AppUser();
					admin.setUsername("admin");
					admin.setPasswordHash(passwordEncoder.encode("admin123!"));
					admin.setRole(Role.ADMIN);
					admin.setEnabled(true);
					userRepository.save(admin);

					AppUser hr = new AppUser();
					hr.setUsername("hr");
					hr.setPasswordHash(passwordEncoder.encode("hr123!"));
					hr.setRole(Role.HR);
					hr.setEnabled(true);
					userRepository.save(hr);
				}
			}
			catch (DataAccessException ex) {
				// If schema isn't ready for some reason, don't prevent app startup.
			}
		};
	}
}

