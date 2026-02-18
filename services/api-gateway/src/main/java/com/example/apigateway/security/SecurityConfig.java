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
