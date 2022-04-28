package com.cc.teams.contacts.server.validation;

/**
 * Thrown when a teams-contacts field value invalidates a constraint.
 * 
 */
public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 2610987032813709814L;

	public ValidationException(String message) {
		super(message);
	}
}
