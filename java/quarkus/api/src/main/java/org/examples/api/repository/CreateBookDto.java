package org.examples.api.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.OffsetDateTime;
import java.util.Set;
import org.examples.api.repository.genres.GenreResponseDto;

public record CreateBookDto(
	/*
	 * Creamos una validación con la librería de hibernate-validator. El paquete `jakarta.validation.constraints` tiene
	 * muchos validadores predefinidos. Se le puede indicar el mensaje ad hoc. Es importante que luego se le indique
	 * que tiene que validarlo, bien con la notación @Valid en donde se use o bien creando un validador específico y
	 * llamándole. Se puede ver en el `BookResource` el ejemplo sencillo con la notación @Valid.
	 */
	@NotBlank String title,
	@NotBlank String author,
	@Past @JsonProperty("publication_date") OffsetDateTime pubDate,
	Set<GenreResponseDto> genres) {

}
