package com.example.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway service (strangler pattern).
 *
 * Responsibilities:
 * - Keep client-facing URLs stable.
 * - Enforce Basic Auth at the gateway (initial migration step).
 * - Route /application/person/** to the new person-service.
 * - Route all other paths to the monolith (pets_backend) by default.
 */
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}
