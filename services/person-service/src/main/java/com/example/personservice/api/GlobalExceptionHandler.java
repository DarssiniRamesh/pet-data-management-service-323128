package com.example.personservice.api;

import com.example.personservice.person.PersonServiceError;
import com.example.personservice.person.PersonServiceException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(PersonServiceException.class)
	public ResponseEntity<ApiResponse<ApiError>> handlePersonServiceException(PersonServiceException ex) {
		HttpStatus status = switch (ex.getError()) {
			case NOT_FOUND -> HttpStatus.NOT_FOUND;
			case EMAIL_ALREADY_EXISTS -> HttpStatus.CONFLICT;
		};

		ApiError err = new ApiError(ex.getError().name(), ex.getMessage(), null);
		return ResponseEntity.status(status).body(new ApiResponse<>(false, ex.getMessage(), err));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<ApiError>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		Map<String, String> fieldErrors = new LinkedHashMap<>();
		for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
			// keep first error per field for stability
			fieldErrors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
		}

		ApiError err = new ApiError("VALIDATION_ERROR", "validation failed", fieldErrors);
		return ResponseEntity.badRequest().body(new ApiResponse<>(false, "validation failed", err));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<ApiError>> handleConstraintViolation(ConstraintViolationException ex) {
		ApiError err = new ApiError("VALIDATION_ERROR", "validation failed", null);
		return ResponseEntity.badRequest().body(new ApiResponse<>(false, "validation failed", err));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<ApiError>> handleGeneric(Exception ex) {
		ApiError err = new ApiError("INTERNAL_ERROR", "internal server error", null);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ApiResponse<>(false, "internal server error", err));
	}
}
