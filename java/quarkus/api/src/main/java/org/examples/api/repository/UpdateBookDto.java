package org.examples.api.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.OffsetDateTime;
import java.util.Set;
import org.examples.api.repository.genres.GenreResponseDto;

public record UpdateBookDto(
	@NotBlank String title,
	@NotBlank String author,
	@Past @JsonProperty("publication_date") OffsetDateTime pubDate,
	Set<GenreResponseDto> genres) {

}
