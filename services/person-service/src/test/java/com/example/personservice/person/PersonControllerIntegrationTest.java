package com.example.personservice.person;

import com.example.personservice.person.dto.PersonCreateRequest;
import com.example.personservice.person.dto.PersonUpdateRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests focusing on preserving the monolith-like contract:
 * - path prefix: /application/person/**
 * - response envelope: { success, message, data }
 */
@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void create_get_update_delete_flow() throws Exception {
		// create
		String createBody = objectMapper.writeValueAsString(new PersonCreateRequest("Ada", "Lovelace", "flow@example.com"));
		String createJson = mvc.perform(post("/application/person")
						.contentType(MediaType.APPLICATION_JSON)
						.content(createBody))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.id").exists())
				.andReturn()
				.getResponse()
				.getContentAsString();

		JsonNode created = objectMapper.readTree(createJson);
		long id = created.at("/data/id").asLong();
		assertThat(id).isPositive();

		// get
		mvc.perform(get("/application/person/{id}", id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.email").value("flow@example.com"));

		// update
		String updateBody = objectMapper.writeValueAsString(new PersonUpdateRequest("Ada", "Byron", "flow@example.com"));
		mvc.perform(put("/application/person/{id}", id)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.lastName").value("Byron"));

		// delete
		mvc.perform(delete("/application/person/{id}", id))
				.andExpect(status().isNoContent());

		// get after delete -> 404 with envelope
		mvc.perform(get("/application/person/{id}", id))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.data.code").value("NOT_FOUND"));
	}

	@Test
	void list_is_paginated_and_wrapped() throws Exception {
		// create a few
		for (int i = 0; i < 3; i++) {
			String body = objectMapper.writeValueAsString(
					new PersonCreateRequest("P" + i, "L" + i, "p" + i + "@example.com")
			);
			mvc.perform(post("/application/person")
							.contentType(MediaType.APPLICATION_JSON)
							.content(body))
					.andExpect(status().isCreated());
		}

		mvc.perform(get("/application/person?page=0&size=2&sort=id&dir=asc"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.data.items").isArray())
				.andExpect(jsonPath("$.data.items.length()").value(2))
				.andExpect(jsonPath("$.data.page").value(0))
				.andExpect(jsonPath("$.data.size").value(2))
				.andExpect(jsonPath("$.data.totalItems").value(3));
	}

	@Test
	void validation_errors_are_wrapped() throws Exception {
		// missing required fields
		String badBody = """
				{
				  "firstName": "",
				  "lastName": "",
				  "email": "not-an-email"
				}
				""";

		mvc.perform(post("/application/person")
						.contentType(MediaType.APPLICATION_JSON)
						.content(badBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.data.code").value("VALIDATION_ERROR"))
				.andExpect(jsonPath("$.data.fieldErrors.firstName").exists())
				.andExpect(jsonPath("$.data.fieldErrors.lastName").exists())
				.andExpect(jsonPath("$.data.fieldErrors.email").exists());
	}

	@Test
	void duplicate_email_is_conflict_and_wrapped() throws Exception {
		String body1 = objectMapper.writeValueAsString(new PersonCreateRequest("A", "B", "conflict@example.com"));
		String body2 = objectMapper.writeValueAsString(new PersonCreateRequest("C", "D", "conflict@example.com"));

		mvc.perform(post("/application/person").contentType(MediaType.APPLICATION_JSON).content(body1))
				.andExpect(status().isCreated());

		mvc.perform(post("/application/person").contentType(MediaType.APPLICATION_JSON).content(body2))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.success").value(false))
				.andExpect(jsonPath("$.data.code").value("EMAIL_ALREADY_EXISTS"));
	}
}
