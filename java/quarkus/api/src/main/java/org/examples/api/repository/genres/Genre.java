package org.examples.api.repository.genres;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import org.examples.api.repository.Book;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
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
