package com.example.personservice.person;

/**
 * Domain error codes for Person service, used for mapping to HTTP status codes.
 */
public enum PersonServiceError {
	NOT_FOUND,
	EMAIL_ALREADY_EXISTS
}
