package com.example.personservice.person;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
	Optional<PersonEntity> findByEmail(String email);

	boolean existsByEmail(String email);
}
