package com.cc.teams.contacts.server.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring Boot main class for bootstrapping the application.
 */
@SpringBootApplication(scanBasePackages = { "com.cc.teams.contacts.server" })
@EntityScan(basePackages = { "com.cc.teams.contacts.server.model" })
@EnableJpaRepositories(basePackages = { "com.cc.teams.contacts.server.dao" })
public class TeamsContactsServerApplication {
	/**
	 * Spring boot application bootstrap main method.
	 *
	 * @param args
	 *            program arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(TeamsContactsServerApplication.class, args);
	}
}
