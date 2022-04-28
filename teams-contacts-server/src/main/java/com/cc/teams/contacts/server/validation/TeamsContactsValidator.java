package com.cc.teams.contacts.server.validation;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Contains utility methods for validating dsm resources.
 */
public class TeamsContactsValidator {
	@Autowired
	private Validator validator;

	public void validateResource(Object resource) throws ValidationException {
		Set<ConstraintViolation<Object>> violations = validator.validate(resource);
		if (CollectionUtils.isNotEmpty(violations)) {
			throw new ValidationException(
					violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("\n")));
		}
	}
}
