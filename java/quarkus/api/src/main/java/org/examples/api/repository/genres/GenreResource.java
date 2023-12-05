package org.examples.api.repository.genres;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.net.URI;
import org.examples.api.PaginatedResponse;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/books/genres")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GenreResource {

	@Inject
	private GenreRepository repo;

	@GET
	public Uni<RestResponse<PaginatedResponse<Genre>>> list(
		@QueryParam("name") String name,
		@QueryParam("page") @DefaultValue("1") int page,
		@QueryParam("pageSize") @DefaultValue("3") int pageSize
	) {
		var p = new Page(page - 1, pageSize);
		if (name != null) {
			var query = "name ILIKE ?1";

			return repo.find(query, name) // Query param by index -> https://quarkus.io/guides/hibernate-orm-panache#query-parameters
				.page(p)
				.pageCount()
				.flatMap(totalPages ->
					repo.find(query, name)
						.list()
						.map(genres -> RestResponse.ok(new PaginatedResponse<>(page, page, genres))));
		}

		return repo.findAll()
			.page(p)
			.pageCount()
			.flatMap(totalPages ->
				repo.findAll()
					.list()
					.map(genres -> RestResponse.ok(new PaginatedResponse<>(page, page, genres))));
	}

	@POST
	@WithTransaction
	public Uni<RestResponse<URI>> create(Genre genre) {
		return repo.persistAndFlush(genre)
			.map(res -> RestResponse.created(URI.create("/books/genres" + genre.getId())));
		// .onFailure().transform(err -> new BadRequestException()); // This is for custom exception handling
	}
}
