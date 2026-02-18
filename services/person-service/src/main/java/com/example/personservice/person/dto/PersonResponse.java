package com.example.personservice.person.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PersonResponse", description = "Person representation returned by the API")
public record PersonResponse(
		@Schema(description = "Person id", example = "1") Long id,
		@Schema(description = "First name", example = "Ada") String firstName,
		@Schema(description = "Last name", example = "Lovelace") String lastName,
		@Schema(description = "Email address", example = "ada@example.com") String email
) {
}
