package org.examples.api.repository;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.common.ProjectedFieldName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.OffsetDateTime;
import java.util.Set;
import org.examples.api.repository.genres.GenreResponseDto;

/*
 * La notación @RegisterForReflection se debe indicar si usamos la proyección.
 *
 * Lo hace automáticamente, localiza a traves de los parámetros que tiene el constructor las propiedades dentro de la
 * clase que estamos proyectando. Así por ejemplo si estamos proyectando una clase `Book` en una clase
 * `BookResponseDto`, y en el constructor tenemos estas propiedades, irá mirando `Book.getId()` `Book.getTitle()`
 * `Book.getAuthor()` `Book.getPubDate()` y `Book.getGenres()`.
 *
 * En el caso de la propiedad `pubDate` al no llamarse de la misma manera en el constructor le tenemos que indicar
 * cómo se llama dicha propiedad en la clase que estamos localizando, como en la clase `Book` la propiedad es `pubDate`
 * se lo indicaremos con la notación @ProjectedFieldName.
 */

@RegisterForReflection
public record BookResponseDto(
	Long id,
	String title,
	String author,
	@ProjectedFieldName("pubDate") @JsonProperty("publication_date") OffsetDateTime publicationDate,
	Set<GenreResponseDto> genres) {

}
