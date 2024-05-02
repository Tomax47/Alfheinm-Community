package com.alfheim.aflheim_community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AflheimCommunityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AflheimCommunityApplication.class, args);
	}

}
