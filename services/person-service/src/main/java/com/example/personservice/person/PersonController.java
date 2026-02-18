package com.example.personservice.person;

import com.example.personservice.api.ApiResponse;
import com.example.personservice.api.PageResponse;
import com.example.personservice.person.dto.PersonCreateRequest;
import com.example.personservice.person.dto.PersonResponse;
import com.example.personservice.person.dto.PersonUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application/person")
@Tag(name = "Person", description = "Person endpoints (extracted microservice), preserving monolith path contract.")
public class PersonController {

	private final PersonService service;

	public PersonController(PersonService service) {
		this.service = service;
	}

	// PUBLIC_INTERFACE
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(
			summary = "Create person",
			description = "Creates a person. Preserves monolith-style response envelope."
	)
	public ApiResponse<PersonResponse> create(@Valid @RequestBody PersonCreateRequest request) {
		/** Create a person and return wrapped response. */
		return ApiResponse.ok(service.create(request));
	}

	// PUBLIC_INTERFACE
	@GetMapping("/{id}")
	@Operation(
			summary = "Get person by id",
			description = "Fetches a person by id. Preserves monolith-style response envelope."
	)
	public ApiResponse<PersonResponse> getById(
			@Parameter(description = "Person id") @PathVariable long id
	) {
		/** Get a person by id and return wrapped response. */
		return ApiResponse.ok(service.getById(id));
	}

	// PUBLIC_INTERFACE
	@GetMapping
	@Operation(
			summary = "List persons (paginated)",
			description = "Returns a paginated list of persons. Query params: page (0-based), size, sort."
	)
	public ApiResponse<PageResponse<PersonResponse>> list(
			@Parameter(description = "0-based page index") @RequestParam(defaultValue = "0") int page,
			@Parameter(description = "page size (1..100)") @RequestParam(defaultValue = "20") int size,
			@Parameter(description = "sort field (id,firstName,lastName,email)") @RequestParam(defaultValue = "id") String sort,
			@Parameter(description = "sort direction (asc|desc)") @RequestParam(defaultValue = "asc") String dir
	) {
		/** List persons with pagination and return wrapped response. */
		int safeSize = Math.min(Math.max(size, 1), 100);
		Sort.Direction direction = "desc".equalsIgnoreCase(dir) ? Sort.Direction.DESC : Sort.Direction.ASC;
		Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, Sort.by(direction, sort));

		return ApiResponse.ok(PageResponse.of(service.list(pageable)));
	}

	// PUBLIC_INTERFACE
	@PutMapping("/{id}")
	@Operation(
			summary = "Update person",
			description = "Updates a person by id (PUT semantics). Preserves monolith-style response envelope."
	)
	public ApiResponse<PersonResponse> update(
			@Parameter(description = "Person id") @PathVariable long id,
			@Valid @RequestBody PersonUpdateRequest request
	) {
		/** Update a person and return wrapped response. */
		return ApiResponse.ok(service.update(id, request));
	}

	// PUBLIC_INTERFACE
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(
			summary = "Delete person",
			description = "Deletes a person by id."
	)
	public void delete(@Parameter(description = "Person id") @PathVariable long id) {
		/** Delete a person by id. */
		service.delete(id);
	}
}
