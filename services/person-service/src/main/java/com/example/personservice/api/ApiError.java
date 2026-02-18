package com.example.personservice.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(name = "ApiError", description = "Error details payload")
public record ApiError(
		@Schema(description = "Machine-readable error code") String code,
		@Schema(description = "Human-readable error message") String message,
		@Schema(description = "Field validation errors (field -> message)") Map<String, String> fieldErrors
) {
}
