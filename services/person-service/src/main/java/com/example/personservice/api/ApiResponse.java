package com.example.personservice.api;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Standard response envelope used by the extracted microservice to preserve the
 * monolith contract expectations (single top-level wrapper object).
 *
 * @param <T> payload type
 */
@Schema(name = "ApiResponse", description = "Standard response envelope")
public record ApiResponse<T>(
		@Schema(description = "Whether the request was successful") boolean success,
		@Schema(description = "Optional human-readable message") String message,
		@Schema(description = "Wrapped payload") T data
) {
	// PUBLIC_INTERFACE
	public static <T> ApiResponse<T> ok(T data) {
		/** Build a success response envelope. */
		return new ApiResponse<>(true, null, data);
	}

	// PUBLIC_INTERFACE
	public static <T> ApiResponse<T> ok(String message, T data) {
		/** Build a success response envelope with a message. */
		return new ApiResponse<>(true, message, data);
	}

	// PUBLIC_INTERFACE
	public static <T> ApiResponse<T> error(String message) {
		/** Build an error response envelope. */
		return new ApiResponse<>(false, message, null);
	}
}
