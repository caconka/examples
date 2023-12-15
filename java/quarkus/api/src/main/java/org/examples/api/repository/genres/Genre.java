package org.examples.api.repository.genres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.UpdateTimestamp;

/*
 * Ejemplo de filtros dinámicos con nombre -> https://www.baeldung.com/hibernate-dynamic-mapping#parameterized-filtering-with-filter
 */

@Entity
@FilterDef(
	name = "name.like", // Nombre descriptivo, ya que nos referiremos a él al utilizarlo
	parameters = @ParamDef(name = "name", type = String.class)) // Nuestra condición require un parámetro, se debe definir
@Filter(name = "name.like", condition = "LOWER(name) LIKE LOWER(:name)") // Condición escrita en HQL
public class Genre {

	@Id
	@SequenceGenerator(name = "genreSeq", sequenceName = "genre_seq", allocationSize = 1)
	@GeneratedValue(generator = "genreSeq")
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	/*
	 * En el caso de querer hacer la relación bidireccional, el propietario de la relación es la entidad `Book` por lo que
	 * en la relación bidireccional debemos indicar el `mappedBy` el campo del owner. En este caso `genres` de la endidad
	 * `Book`.
	 */
//	@ManyToMany(mappedBy = "genres", fetch = FetchType.EAGER)
//	private Set<Book> books = new HashSet<>();

	@CreationTimestamp
	@Column(name = "created_at")
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	public Genre() {
	}

	public Genre(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(OffsetDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Genre{" +
			"id=" + id +
			", name='" + name + '\'' +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}

}
