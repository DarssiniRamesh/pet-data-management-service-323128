package com.example.personservice.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Simple page wrapper for consistent pagination payload.
 *
 * @param <T> item type
 */
@Schema(name = "PageResponse", description = "Paginated response payload")
public record PageResponse<T>(
		@Schema(description = "Items on the current page") List<T> items,
		@Schema(description = "Zero-based page index") int page,
		@Schema(description = "Page size") int size,
		@Schema(description = "Total items count") long totalItems,
		@Schema(description = "Total pages count") int totalPages
) {
	// PUBLIC_INTERFACE
	public static <T> PageResponse<T> of(org.springframework.data.domain.Page<T> page) {
		/** Convert a Spring Page into a serializable DTO. */
		return new PageResponse<>(
				page.getContent(),
				page.getNumber(),
				page.getSize(),
				page.getTotalElements(),
				page.getTotalPages()
		);
	}
}
