package org.examples.api.repository;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
import org.examples.api.repository.genres.GenreMapper;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@WithTransaction
public class BookResource {

	private final BookRepository repo;
	private final BookMapper bookMapper;
	private final GenreMapper genreMapper;

	@Inject
	public BookResource(BookRepository repo, BookMapper bookMapper, GenreMapper genreMapper) {
		this.repo = repo;
		this.bookMapper = bookMapper;
		this.genreMapper = genreMapper;
	}

	@GET
	public Uni<RestResponse<PaginatedResponse<BookResponseDto>>> list(
		@QueryParam("title") String title,
		@QueryParam("genre") String genre,
		@QueryParam("page") @DefaultValue("1") int page,
		@QueryParam("pageSize") @DefaultValue("3") int pageSize
	) {
		return repo.findPage(page, pageSize, title, genre)
			.map(bookMapper::toBookResponseDto)
			.map(RestResponse::ok);
	}

	/*
	 * Se le debe indicar la notación @Valid para que haga las validaciones que tengamos en la clase.
	 */
	@POST
	public Uni<RestResponse<URI>> create(@Valid CreateBookDto dto) {
		var book = bookMapper.toBook(dto);

		return repo.persistAndFlush(book)
			.map(res -> RestResponse.created(URI.create("/books/" + book.getId())));
		// .onFailure().transform(err -> new BadRequestException()); // This is for custom exception handling
	}

	@GET
	@Path("/{id}")
	public Uni<RestResponse<JsonObject>> getSingle(Long id) {
		return repo.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new)
			.map(bookMapper::toBookResponseDto)
			.map(book -> RestResponse.ok(new JsonObject().put("data", book)));
	}

	@PUT
	@Path("/{id}")
	public Uni<RestResponse<JsonObject>> update(Long id, @Valid UpdateBookDto dto) {
		return repo.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new)
			.flatMap(b -> {
				if (dto.title() != null) {
					b.setTitle(dto.title());
				}
				if (dto.pubDate() != null) {
					b.setPubDate(dto.pubDate());
				}
				if (dto.author() != null) {
					b.setAuthor(dto.author());
				}
				if (dto.genres() != null) {
					b.setGenres(genreMapper.toGenres(dto.genres()));
				}

				return repo.persistAndFlush(b);
			})
			.map(b -> RestResponse.accepted(new JsonObject().put("data", b)))
			.onFailure().transform(err -> new BadRequestException());
	}
}
