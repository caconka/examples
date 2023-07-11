package org.examples.fruits;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Fruit extends PanacheEntity {

	@Column(unique = true, nullable = false)
	public String name;

	@Column(nullable = false)
	public String color;

	public String season;

}
