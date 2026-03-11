package com.edgar.recruitment_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RecruitmentBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecruitmentBackendApplication.class, args);
	}

}
