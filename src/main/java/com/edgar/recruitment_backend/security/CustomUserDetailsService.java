package com.edgar.recruitment_backend.security;

import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.edgar.recruitment_backend.repo.AppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final AppUserRepository appUserRepository;

	public CustomUserDetailsService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		var user = appUserRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
		return new User(user.getUsername(), user.getPasswordHash(), user.isEnabled(), true, true, true,
				Collections.singleton(authority));
	}
}

