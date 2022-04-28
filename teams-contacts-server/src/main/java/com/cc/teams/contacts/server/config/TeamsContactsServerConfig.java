/*
 * Copyright (c) 2021 ANSYS Germany GmbH
 * All rights reserved.
 */
package com.cc.teams.contacts.server.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cc.teams.contacts.server.validation.TeamsContactsValidator;

@Configuration
public class TeamsContactsServerConfig {
	@Bean
	public TeamsContactsValidator resourceValidator() {
		return new TeamsContactsValidator();
	}

	@Bean
	public Validator beansValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}
}
