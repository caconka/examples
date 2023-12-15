package org.examples.api;

import java.util.List;

public record PaginatedResponse<E>(int page, int totalPages, List<E> data) {
}
