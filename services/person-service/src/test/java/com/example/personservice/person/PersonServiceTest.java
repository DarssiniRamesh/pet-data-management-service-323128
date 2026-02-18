package com.example.personservice.person;

import com.example.personservice.person.dto.PersonCreateRequest;
import com.example.personservice.person.dto.PersonUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@Import(PersonService.class)
class PersonServiceTest {

	@Autowired
	private PersonRepository repository;

	@Autowired
	private PersonService service;

	@Test
	void create_and_getById_roundtrip() {
		var created = service.create(new PersonCreateRequest("Ada", "Lovelace", "ada@example.com"));

		assertThat(created.id()).isNotNull();
		assertThat(created.email()).isEqualTo("ada@example.com");

		var fetched = service.getById(created.id());
		assertThat(fetched.firstName()).isEqualTo("Ada");
	}

	@Test
	void list_is_paginated() {
		service.create(new PersonCreateRequest("A", "One", "a1@example.com"));
		service.create(new PersonCreateRequest("B", "Two", "b2@example.com"));
		service.create(new PersonCreateRequest("C", "Three", "c3@example.com"));

		var page = service.list(PageRequest.of(0, 2));
		assertThat(page.getContent()).hasSize(2);
		assertThat(page.getTotalElements()).isEqualTo(3);
	}

	@Test
	void update_changes_fields() {
		var created = service.create(new PersonCreateRequest("Ada", "Lovelace", "ada2@example.com"));
		var updated = service.update(created.id(), new PersonUpdateRequest("Ada", "Byron", "ada2@example.com"));

		assertThat(updated.lastName()).isEqualTo("Byron");
	}

	@Test
	void delete_removes_entity() {
		var created = service.create(new PersonCreateRequest("Ada", "Lovelace", "ada3@example.com"));

		service.delete(created.id());

		assertThatThrownBy(() -> service.getById(created.id()))
				.isInstanceOf(PersonServiceException.class)
				.satisfies(ex -> assertThat(((PersonServiceException) ex).getError()).isEqualTo(PersonServiceError.NOT_FOUND));
	}

	@Test
	void create_duplicate_email_returns_conflict_error_code() {
		service.create(new PersonCreateRequest("Ada", "Lovelace", "dup@example.com"));

		assertThatThrownBy(() -> service.create(new PersonCreateRequest("Other", "Person", "dup@example.com")))
				.isInstanceOf(PersonServiceException.class)
				.satisfies(ex -> assertThat(((PersonServiceException) ex).getError()).isEqualTo(PersonServiceError.EMAIL_ALREADY_EXISTS));
	}
}
