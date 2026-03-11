package com.edgar.recruitment_backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edgar.recruitment_backend.config.AppProperties;

@Service
public class CvStorageService {

	private final Path rootDir;

	public CvStorageService(AppProperties appProperties) {
		this.rootDir = Path.of(appProperties.storage().cvDir()).toAbsolutePath().normalize();
	}

	public String store(MultipartFile file) throws IOException {
		Files.createDirectories(rootDir);
		String filename = "app-" + java.util.UUID.randomUUID() + "-" + sanitize(file.getOriginalFilename());
		Path target = rootDir.resolve(filename);
		Files.copy(file.getInputStream(), target);
		return target.toString();
	}

	public byte[] load(String storagePath) throws IOException {
		return Files.readAllBytes(Path.of(storagePath));
	}

	private String sanitize(String original) {
		if (original == null) {
			return "cv.pdf";
		}
		return original.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
	}
}

