package org.examples.api.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
}
