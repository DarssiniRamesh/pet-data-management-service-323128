package com.example.personservice.person;

public class PersonServiceException extends RuntimeException {

	private final PersonServiceError error;

	public PersonServiceException(PersonServiceError error, String message) {
		super(message);
		this.error = error;
	}

	public PersonServiceError getError() {
		return error;
	}
}
