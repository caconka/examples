package org.examples.api.repository.genres;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/*
 * Context and dependency injection (CDI) -> https://quarkus.io/guides/cdi
 */
@ApplicationScoped
public class GenreRepository implements PanacheRepository<Genre> {
}
