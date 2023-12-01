package org.examples.api.repository;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.net.URI;
import org.examples.api.PaginatedResponse;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

	@Inject
	private BookRepository repo;

	@GET
	public Uni<RestResponse<PaginatedResponse<Book>>> list(
		@QueryParam("title") String title,
		@QueryParam("genre") String genre,
		@QueryParam("page") @DefaultValue("1") int page,
		@QueryParam("pageSize") @DefaultValue("3") int pageSize
	) {
		if (pageSize > 10) {
			pageSize = 10;
		}

		var p = new Page(page - 1, pageSize);

		if (title != null) {
			var likeTitle = "%" + title + "%";
			var query = "title ILIKE ?1 order by title asc";

			return repo.find(query, likeTitle)
				.page(p)
				.pageCount()
				.flatMap(totalPages ->
					repo.find(query, likeTitle)
						.page(p)
						.list()
						.map(books -> RestResponse.ok(new PaginatedResponse<>(page, totalPages, books))));
		}

		return repo.find("")
			.page(p)
			.pageCount()
			.flatMap(totalPages ->
				repo.findAll(Sort.by("title", Direction.Ascending))
					.page(p)
					.list()
					.map(books -> RestResponse.ok(new PaginatedResponse<>(page, totalPages, books))));
	}

	@POST
	@WithTransaction
	public Uni<RestResponse<URI>> create(Book book) {
		return repo.persistAndFlush(book)
			.map(res -> RestResponse.created(URI.create("/books/" + book.getId())));
		// .onFailure().transform(err -> new BadRequestException()); // This is for custom exception handling
	}

	@GET
	@Path("/{id}")
	public Uni<RestResponse<JsonObject>> getSingle(Long id) {
		return repo.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new)
			.map(book -> RestResponse.ok(new JsonObject().put("data", book)));
	}

	@PUT
	@Path("/{id}")
	@WithTransaction
	public Uni<RestResponse<JsonObject>> update(Long id, Book book) {
		return repo.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new)
			.flatMap(b -> {
				if (book.getTitle() != null) {
					b.setTitle(book.getTitle());
				}
				if (book.getPubDate() != null) {
					b.setPubDate(book.getPubDate());
				}
				if (book.getAuthor() != null) {
					b.setAuthor(book.getAuthor());
				}

				return repo.persistAndFlush(b);
			})
			.map(b -> RestResponse.accepted(new JsonObject().put("data", b)))
			.onFailure().transform(err -> new BadRequestException());
	}
}
