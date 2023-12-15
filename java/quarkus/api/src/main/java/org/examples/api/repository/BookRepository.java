package org.examples.api.repository;

import io.quarkus.hibernate.reactive.panache.PanacheQuery;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.examples.api.PaginatedResponse;

/*
 * Context and dependency injection (CDI) -> https://quarkus.io/guides/cdi
 */
@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {

	public Uni<PaginatedResponse<Book>> findPage(int page, int pageSize, String title, String genre) {
		if (pageSize > 10) {
			pageSize = 10;
		}

		/*
		 * Query parameters -> https://quarkus.io/guides/hibernate-orm-panache#query-parameters
		 * We can create query params by index (1-based) (?1|?2|...) or by name (:title|:genre|...)
		 */

		var p = new Page(page - 1, pageSize);
		var params = createQueryParamsMap(title, genre);
		var queryStr = createQueryStr(title, genre);
		var query = find(queryStr, Sort.by("title", Direction.Ascending), params).page(p);

		return query
			.pageCount()
			.flatMap(totalPages ->
				query.list()
					.map(books -> new PaginatedResponse<>(page, totalPages, books)));
	}

	private String createQueryStr(String title, String genre) {
		var queryStr = "";

		if (title != null) {
			queryStr += "title ILIKE :title ";
		}

		if (genre != null) {
			var queryWithGenre = "genre ILIKE :genre";
			queryStr += !queryStr.isBlank() ? " AND " + queryWithGenre : queryWithGenre;
		}

		return queryStr;
	}

	private Parameters createQueryParamsMap(String title, String genre) {
		var params = new Parameters();

		if (title != null) {
			params.and("title", likeValue(title));
		}

		if (genre != null) {
			params.and("genre", likeValue(genre));
		}

		return params;
	}

	private String likeValue(String value) {
		return "%" + value + "%";
	}

}
