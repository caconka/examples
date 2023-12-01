package org.examples.api.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Book {

	/*
	 * Repository pattern https://quarkus.io/guides/hibernate-reactive-panache#solution-2-using-the-repository-pattern
	 * En este caso se debe indicar el campo id
	 */

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, unique = true)
	private String title;

	@Column(nullable = false)
	private String author;

	@Column(name = "publication_date", nullable = false)
	private OffsetDateTime pubDate;

	@CreationTimestamp
	@Column(name = "create_date")
	private OffsetDateTime createDate;

	@UpdateTimestamp
	@Column(name = "update_date")
	private OffsetDateTime updateDate;

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Book setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getAuthor() {
		return author;
	}

	public Book setAuthor(String author) {
		this.author = author;
		return this;
	}

	public OffsetDateTime getPubDate() {
		return pubDate;
	}

	public Book setPubDate(OffsetDateTime pubDate) {
		this.pubDate = pubDate;
		return this;
	}

	public OffsetDateTime getCreateDate() {
		return createDate;
	}

	public OffsetDateTime getUpdateDate() {
		return updateDate;
	}

	@Override
	public String toString() {
		return "Book{" +
			"id=" + id +
			", title='" + title + '\'' +
			", author='" + author + '\'' +
			", pubDate=" + pubDate +
			", createDate=" + createDate +
			", updateDate=" + updateDate +
			'}';
	}

}
