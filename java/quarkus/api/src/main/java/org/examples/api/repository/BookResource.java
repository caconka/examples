package org.examples.api.repository;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.net.URI;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

	@Inject
	private BookRepository repo;

	@GET
	public Uni<RestResponse<JsonObject>> getAll() {
		return repo.listAll(Sort.by("title")).map(books -> RestResponse.ok(new JsonObject().put("data", books)));
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
				if (book.getTitle() != null) b.setTitle(book.getTitle());
				if (book.getPubDate() != null) b.setPubDate(book.getPubDate());
				if (book.getAuthor() != null) b.setAuthor(book.getAuthor());

				return repo.persistAndFlush(b);
			})
			.map(b -> RestResponse.accepted(new JsonObject().put("data", b)))
			.onFailure().transform(err -> new BadRequestException());
	}
}
