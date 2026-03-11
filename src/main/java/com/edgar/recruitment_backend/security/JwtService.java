package com.edgar.recruitment_backend.security;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.edgar.recruitment_backend.config.AppProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final AppProperties appProperties;
	private final byte[] secretKey;

	public JwtService(AppProperties appProperties) {
		this.appProperties = appProperties;
		this.secretKey = Decoders.BASE64.decode(
				java.util.Base64.getEncoder().encodeToString(appProperties.jwt().secret().getBytes()));
	}

	public String generateToken(String username, String role) {
		var now = Instant.now();
		var expiry = now.plus(appProperties.jwt().accessTokenTtl());

		return Jwts.builder()
				.issuer(appProperties.jwt().issuer())
				.subject(username)
				.claim("role", role)
				.issuedAt(Date.from(now))
				.expiration(Date.from(expiry))
				.claims(Map.of())
				.signWith(Keys.hmacShaKeyFor(secretKey))
				.compact();
	}

	public String extractUsername(String token) {
		return Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(secretKey))
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}

	public String extractRole(String token) {
		return (String) Jwts.parser()
				.verifyWith(Keys.hmacShaKeyFor(secretKey))
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.get("role");
	}
}

