package org.examples.api;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import java.util.List;

public record PaginatedResponse<E>(int page, int totalPages, List<E> data) {
}
