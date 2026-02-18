package com.example.apigateway.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight system endpoints for the gateway.
 */
@RestController
@Tag(name = "System", description = "System endpoints for api-gateway")
public class SystemController {

	@GetMapping("/health")
	@Operation(summary = "Health check", description = "Returns gateway health status")
	public String health() {
		return "OK";
	}

	@GetMapping("/docs")
	@Operation(summary = "API Documentation", description = "Redirects to Swagger UI")
	public ResponseEntity<Void> docs() {
		return ResponseEntity.status(HttpStatus.FOUND)
				.header("Location", "/swagger-ui.html")
				.build();
	}
}
