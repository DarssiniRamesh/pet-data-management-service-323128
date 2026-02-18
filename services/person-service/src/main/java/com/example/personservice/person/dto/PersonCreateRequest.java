package com.example.personservice.person.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "PersonCreateRequest", description = "Payload to create a person")
public record PersonCreateRequest(
		@Schema(description = "First name", example = "Ada")
		@NotBlank(message = "firstName is required")
		@Size(max = 100, message = "firstName must be at most 100 characters")
		String firstName,

		@Schema(description = "Last name", example = "Lovelace")
		@NotBlank(message = "lastName is required")
		@Size(max = 100, message = "lastName must be at most 100 characters")
		String lastName,

		@Schema(description = "Email address", example = "ada@example.com")
		@NotBlank(message = "email is required")
		@Email(message = "email must be a valid email address")
		@Size(max = 254, message = "email must be at most 254 characters")
		String email
) {
}
