package org.examples.api.repository.genres;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
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

	private final GenreRepository repo;

	@Inject
	public GenreResource(GenreRepository repo) {
		this.repo = repo;
	}

	@GET
	public Uni<RestResponse<PaginatedResponse<GenreResponseDto>>> list(
		@QueryParam("name") String name,
		@QueryParam("page") @DefaultValue("1") int page,
		@QueryParam("pageSize") @DefaultValue("3") int pageSize
	) {
		var p = new Page(page - 1, pageSize);
		var query = repo.findAll(Sort.descending("name")).page(p);

		if (name != null) {
			var params = Parameters.with("name", "%" + name + "%");
			var filterNameLike = "name.like";

			query.filter(filterNameLike, params);
		}

		return query
			.pageCount()
			.flatMap(totalPages ->
				query.project(GenreResponseDto.class) // Ejemplo de proyección, proyectamos `Genre` en `GenreResponseDto`
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
