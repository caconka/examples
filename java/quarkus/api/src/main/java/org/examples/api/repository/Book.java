package org.examples.api.repository;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import org.examples.api.repository.genres.Genre;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Book {

	/*
	 * Repository pattern https://quarkus.io/guides/hibernate-reactive-panache#solution-2-using-the-repository-pattern
	 * En este caso se debe indicar el campo id
	 */

	@Id
	@SequenceGenerator(name = "bookSeq", sequenceName = "book_seq", allocationSize = 1)
	@GeneratedValue(generator = "bookSeq")
	private Long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column(nullable = false)
	private String author;

	@Column(name = "publicated_at", nullable = false)
	private OffsetDateTime pubDate;

	/*
	 * Hibernate associations -> https://www.baeldung.com/jpa-hibernate-associations
	 *
	 * Indicamos el parámetro `cascade` en el tipo de relación para que luego a la hora de persistir no tengamos que
	 * hacerlo por separado si se diese el caso de que el género que estamos insertando no está creado todavía.
	 *
	 * Como estamos utilizando una relación `ManyToMany` debemos indicar la anotación `@JoinTable`.
	 *
	 * `joinColumns` define cómo configurar la columna de unión (clave externa) del lado propietario de la relación en la
	 * tabla. En este caso sería Book.
	 *
	 * Por otro lado, el parámetro `inverseJoinColumns` hace lo mismo, pero para el lado referenciador (Genre).
	 */
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
		name = "book_genre",
		joinColumns = @JoinColumn(name = "book_id"),
		inverseJoinColumns = @JoinColumn(name = "genre_id"))
	private Set<Genre> genres = new HashSet<>();

	@CreationTimestamp
	@Column(name = "created_at")
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public OffsetDateTime getPubDate() {
		return pubDate;
	}

	public void setPubDate(OffsetDateTime pubDate) {
		this.pubDate = pubDate;
	}

	public Set<Genre> getGenres() {
		return genres;
	}

	public void setGenres(Set<Genre> genres) {
		this.genres = genres;
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
		return "Book{" +
			"id=" + id +
			", title='" + title + '\'' +
			", author='" + author + '\'' +
			", pubDate=" + pubDate +
			", genres=" + genres +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			'}';
	}
}
