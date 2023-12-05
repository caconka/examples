package org.examples.api.repository;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NamedEntityGraph;
import java.util.HashMap;
import java.util.Map;
import org.examples.api.PaginatedResponse;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

	public Uni<PaginatedResponse<Book>> findPage(int page, int pageSize, String title, String genre) {
		if (pageSize > 10) {
			pageSize = 10;
		}

		var p = new Page(page - 1, pageSize);
		var query = createQuery(title, genre);
		var queryParams = createQueryParamsMap(title, genre);

		/*
		 * Query parameters -> https://quarkus.io/guides/hibernate-orm-panache#query-parameters
		 * We can create query params by index (1-based) (?1|?2|...) or by name (:title|:genre|...)
		 */

		return find(query, queryParams)
			.page(p)
			.pageCount()
			.flatMap(totalPages ->
				find(query, Sort.by("title", Direction.Ascending), queryParams)
					.page(p)
					.list()
					.map(books -> new PaginatedResponse<>(page, totalPages, books)));
	}

	private Map<String, Object> createQueryParamsMap(String title, String genre) {
		Map<String, Object> map = new HashMap<>();

		if (title != null) {
			map.put("title", likeValue(title));
		}

		if (genre != null) {
			map.put("genre", likeValue(genre));
		}

		return map;
	}

	private String createQuery(String title, String genre) {
		var query = "";

		if (title != null) {
			query += "title ILIKE :title ";
		}

		if (genre != null) {
			var queryWithGenre = "genre ILIKE :genre";
			query += !query.isBlank() ? " AND " + queryWithGenre : queryWithGenre;
		}

		return query;
	}

	private String likeValue(String value) {
		return "%" + value + "%";
	}

}
