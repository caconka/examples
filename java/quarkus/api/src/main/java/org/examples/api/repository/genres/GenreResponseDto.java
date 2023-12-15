package org.examples.api.repository.genres;

import io.quarkus.runtime.annotations.RegisterForReflection;

/*
 * La notación @RegisterForReflection se debe indicar si usamos la proyección. Lo tengo explicado en la clase
 * `BookResponseDto`. Y el ejemplo del uso en `GenreResource`.
 */

@RegisterForReflection
public record GenreResponseDto(String name) {

}
