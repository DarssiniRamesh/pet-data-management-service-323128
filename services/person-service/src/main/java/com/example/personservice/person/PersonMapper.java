package com.example.personservice.person;

import com.example.personservice.person.dto.PersonCreateRequest;
import com.example.personservice.person.dto.PersonResponse;
import com.example.personservice.person.dto.PersonUpdateRequest;

final class PersonMapper {

	private PersonMapper() {
	}

	static PersonEntity toEntity(PersonCreateRequest req) {
		return new PersonEntity(req.firstName(), req.lastName(), req.email());
	}

	static PersonResponse toResponse(PersonEntity e) {
		return new PersonResponse(e.getId(), e.getFirstName(), e.getLastName(), e.getEmail());
	}

	static void applyUpdate(PersonEntity e, PersonUpdateRequest req) {
		e.setFirstName(req.firstName());
		e.setLastName(req.lastName());
		e.setEmail(req.email());
	}
}
