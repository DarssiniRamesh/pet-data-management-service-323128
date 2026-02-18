package com.example.personservice.person;

import com.example.personservice.person.dto.PersonCreateRequest;
import com.example.personservice.person.dto.PersonResponse;
import com.example.personservice.person.dto.PersonUpdateRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {

	private final PersonRepository repository;

	public PersonService(PersonRepository repository) {
		this.repository = repository;
	}

	@Transactional
	public PersonResponse create(PersonCreateRequest request) {
		try {
			PersonEntity saved = repository.save(PersonMapper.toEntity(request));
			return PersonMapper.toResponse(saved);
		} catch (DataIntegrityViolationException ex) {
			// Likely unique constraint violation for email
			throw new PersonServiceException(PersonServiceError.EMAIL_ALREADY_EXISTS, "email already exists");
		}
	}

	@Transactional(readOnly = true)
	public PersonResponse getById(long id) {
		PersonEntity entity = repository.findById(id)
				.orElseThrow(() -> new PersonServiceException(PersonServiceError.NOT_FOUND, "person not found"));
		return PersonMapper.toResponse(entity);
	}

	@Transactional(readOnly = true)
	public Page<PersonResponse> list(Pageable pageable) {
		return repository.findAll(pageable).map(PersonMapper::toResponse);
	}

	@Transactional
	public PersonResponse update(long id, PersonUpdateRequest request) {
		PersonEntity entity = repository.findById(id)
				.orElseThrow(() -> new PersonServiceException(PersonServiceError.NOT_FOUND, "person not found"));

		PersonMapper.applyUpdate(entity, request);

		try {
			PersonEntity saved = repository.save(entity);
			return PersonMapper.toResponse(saved);
		} catch (DataIntegrityViolationException ex) {
			throw new PersonServiceException(PersonServiceError.EMAIL_ALREADY_EXISTS, "email already exists");
		}
	}

	@Transactional
	public void delete(long id) {
		if (!repository.existsById(id)) {
			throw new PersonServiceException(PersonServiceError.NOT_FOUND, "person not found");
		}
		repository.deleteById(id);
	}
}
