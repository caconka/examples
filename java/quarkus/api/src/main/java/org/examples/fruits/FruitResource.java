package org.examples.fruits;

import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.net.URI;

import org.jboss.resteasy.reactive.RestResponse;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FruitResource {

	@GET
	public Uni<RestResponse<JsonObject>> getAll() {
		return Fruit.listAll(Sort.by("name")).map(fruits -> RestResponse.ok(new JsonObject().put("data", fruits)));
	}

	@POST
	@WithTransaction
	public Uni<RestResponse<URI>> create(Fruit fruit) {
		return fruit.persistAndFlush()
			.map(res -> RestResponse.created(URI.create("/fruits/" + fruit.id)));
			// .onFailure().transform(err -> new BadRequestException()); // This is for custom exception handling
	}

	@GET
	@Path("/{id}")
	public Uni<RestResponse<JsonObject>> getSingle(Long id) {
		return Fruit.findById(id)
			.onItem().ifNull().failWith(NotFoundException::new)
			.map(fruit -> RestResponse.ok(new JsonObject().put("data", fruit)));
	}

	@PUT
	@Path("/{id}")
	@WithTransaction
	public Uni<RestResponse<JsonObject>> update(Long id, Fruit fruit) {
		return Fruit.<Fruit>findById(id)
			.onItem().ifNull().failWith(NotFoundException::new)
			.flatMap(f -> {
				if (fruit.name != null) f.name = fruit.name;
				if (fruit.season != null) f.season = fruit.season;
				if (fruit.color != null) f.color = fruit.color;

				return f.persistAndFlush();
			})
			.map(f -> RestResponse.accepted(new JsonObject().put("data", f)))
			.onFailure().transform(err -> new BadRequestException());
	}
}
