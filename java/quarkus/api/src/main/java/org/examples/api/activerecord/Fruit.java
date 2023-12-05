package org.examples.api.activerecord;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.OffsetDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Fruit extends PanacheEntity {

	/*
	 * Active record pattern https://quarkus.io/guides/hibernate-reactive-panache#solution-1-using-the-active-record-pattern
	 * No se debe indicar el campo `id`
	 */

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String color;

	private String season;

	@CreationTimestamp
	@Column(name = "created_at")
	private OffsetDateTime createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at")
	private OffsetDateTime updatedAt;

	public String getName() {
		return name;
	}

	public Fruit setName(String name) {
		this.name = name;
		return this;
	}

	public String getColor() {
		return color;
	}

	public Fruit setColor(String color) {
		this.color = color;
		return this;
	}

	public String getSeason() {
		return season;
	}

	public Fruit setSeason(String season) {
		this.season = season;
		return this;
	}

	public OffsetDateTime getCreatedAt() {
		return createdAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Fruit{" +
			"name='" + name + '\'' +
			", color='" + color + '\'' +
			", season='" + season + '\'' +
			", createDate=" + createdAt +
			", updateDate=" + updatedAt +
			'}';
	}
}
