package org.example.http.handler;

import static org.example.http.handler.LoggerContextHandler.LOG_CONTEXT;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.rxjava3.ext.web.RoutingContext;
import java.util.Random;
import org.example.entity.logger.Log;
import org.example.entity.logger.LogContext;
import org.example.http.model.User;

public class UserHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserHandler.class);

	private static final Random RANDOM = new Random();

	public void createUser(RoutingContext routingCtx) {
		LogContext logCtx = routingCtx.get(LOG_CONTEXT);
		LOGGER.info(Log.builder().setMessage("CreateUser handler").setContext(logCtx).build().toJson());

		var user = User.builder()
			.setId(RANDOM.nextInt(10000) + "")
			.setUsername("johnJames")
			.setFirstName("John")
			.setLastName("James")
			.setEmail("john@email.com")
			.setPhone(RANDOM.nextInt(1000000000) + "")
			.build();

		routingCtx.response()
			.setStatusCode(HttpResponseStatus.CREATED.code())
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.end(Json.encode(user));
	}

	public void findUserById(RoutingContext routingCtx) {
		LogContext logCtx = routingCtx.get(LOG_CONTEXT);
		LOGGER.info(Log.builder().setMessage("FindUserById handler").setContext(logCtx).build().toJson());

		var user = User.builder()
			.setId(routingCtx.pathParam("id"))
			.setUsername("johnJames")
			.setFirstName("John")
			.setLastName("James")
			.setEmail("john@email.com")
			.setPhone(RANDOM.nextInt(1000000000) + "")
			.build();

		routingCtx.response()
			.setStatusCode(HttpResponseStatus.OK.code())
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.end(Json.encode(user));
	}

	public void deleteUser(RoutingContext routingCtx) {
		LogContext logCtx = routingCtx.get(LOG_CONTEXT);
		LOGGER.info(Log.builder().setMessage("DeleteUser handler").setContext(logCtx).build().toJson());

		routingCtx.response()
			.setStatusCode(HttpResponseStatus.NO_CONTENT.code())
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.end();
	}

}
