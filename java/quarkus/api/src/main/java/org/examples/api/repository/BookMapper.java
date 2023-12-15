package org.examples.api.repository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import java.util.Set;
import org.examples.api.PaginatedResponse;
import org.examples.api.repository.genres.Genre;
import org.examples.api.repository.genres.GenreMapper;
import org.examples.api.repository.genres.GenreResponseDto;

@RequestScoped
public class BookMapper {

	private final GenreMapper genreMapper;

	@Inject
	public BookMapper(GenreMapper genreMapper) {
		this.genreMapper = genreMapper;
	}

	public PaginatedResponse<BookResponseDto> toBookResponseDto(PaginatedResponse<Book> book) {
		var books = book.data().stream().map(this::toBookResponseDto).toList();

		return new PaginatedResponse<>(book.page(), book.totalPages(), books);
	}

	public BookResponseDto toBookResponseDto(Book book) {
		var genres = book.getGenres().stream().map(g -> new GenreResponseDto(g.getName())).toList();

		return new BookResponseDto(book.getId(), book.getTitle(), book.getAuthor(), book.getPubDate(), Set.copyOf(genres));
	}

	public Book toBook(CreateBookDto dto) {
		var book = new Book();

		book.setTitle(dto.title());
		book.setAuthor(dto.author());
		book.setPubDate(dto.pubDate());
		book.setGenres(genreMapper.toGenres(dto.genres()));

		return book;
	}
}
