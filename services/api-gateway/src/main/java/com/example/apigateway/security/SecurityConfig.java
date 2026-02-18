package com.example.apigateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Basic Auth enforced at the gateway (first step of auth centralization).
 *
 * Notes:
 * - Downstream services can remain unauthenticated initially; gateway controls access.
 * - /health, /docs, /swagger-ui.html, /swagger-ui/**, /api-docs/** are permitted anonymously to
 *   make local dev and API discovery easy.
 *
 * Swagger aggregation note:
 * - /api-docs is the gateway's own OpenAPI JSON (springdoc).
 * - /api-docs/{service} are gateway-proxied downstream OpenAPI JSON endpoints (configured as Gateway routes),
 *   and must remain anonymously accessible so Swagger UI can load them.
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(exchanges -> exchanges
						// Allow preflight without auth
						.pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						// Allow basic ops / docs without auth (optional; can tighten later)
						// NOTE: /api-docs/** includes both the gateway's OpenAPI JSON and proxied downstream specs
						// (e.g., /api-docs/person-service, /api-docs/monolith) used by the aggregated Swagger UI dropdown.
						.pathMatchers("/health", "/docs", "/swagger-ui.html", "/swagger-ui/**", "/api-docs/**").permitAll()
						.anyExchange().authenticated()
				)
				.httpBasic(Customizer.withDefaults())
				.build();
	}

	@Bean
	public MapReactiveUserDetailsService userDetailsService(
			@Value("${gateway.security.basic.username:admin}") String username,
			@Value("${gateway.security.basic.password:admin}") String password
	) {
		// NOTE: Password is {noop} for initial dev simplicity. Replace with an encoder later.
		UserDetails user = User.withUsername(username)
				.password("{noop}" + password)
				.roles("USER")
				.build();

		return new MapReactiveUserDetailsService(user);
	}
}
