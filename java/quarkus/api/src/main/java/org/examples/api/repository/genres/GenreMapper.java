package org.examples.api.repository.genres;

import jakarta.enterprise.context.RequestScoped;
import java.util.Set;

@RequestScoped
public class GenreMapper {

	public Genre toGenre(GenreResponseDto dto) {
		return new Genre(dto.name());
	}

	public Set<Genre> toGenres(Set<GenreResponseDto> dto) {
		var genres = dto.stream().map(this::toGenre).toList();

		return Set.copyOf(genres);
	}
}
