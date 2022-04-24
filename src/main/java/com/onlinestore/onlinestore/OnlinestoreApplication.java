package com.onlinestore.onlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class OnlinestoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(OnlinestoreApplication.class, args);
	}
}
