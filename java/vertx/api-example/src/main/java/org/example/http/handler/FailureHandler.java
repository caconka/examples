package org.example.http.handler;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.Json;
import io.vertx.rxjava3.ext.web.RoutingContext;
import java.util.Arrays;
import java.util.Map;
import org.example.entity.exception.BackendException;
import org.example.entity.exception.BadRequestException;
import org.example.entity.exception.Error;
import org.example.entity.exception.InternalServerErrorException;
import org.example.entity.logger.Log;

public class FailureHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FailureHandler.class);

	public void handle(RoutingContext routingCtx) {
		var err = routingCtx.failure();

		LOGGER.error(
			Log.builder()
				.setMessage("Error handling")
				.addMetadata("errorMsg", err.getMessage())
				.addMetadata("detailMsg", err.getLocalizedMessage())
				.addMetadata("cause", err.getCause() + "")
				.addMetadata("stackTrace", Arrays.toString(err.getStackTrace()))
				.build().toJson());

		var body = err.getMessage();

		if (routingCtx.failure() instanceof BackendException exception) {
			body = toResponseBody(exception.getError());
			routingCtx.response().setStatusCode(exception.getHttpCode());

		} else if (routingCtx.statusCode() == HttpResponseStatus.BAD_REQUEST.code()) {

			var badRequestException = new BadRequestException(routingCtx.failure().getLocalizedMessage());
			body = toResponseBody(badRequestException.getError());
			routingCtx.response().setStatusCode(routingCtx.statusCode());

		} else if (routingCtx.statusCode() == HttpResponseStatus.INTERNAL_SERVER_ERROR.code()) {

			var internalServerErrorException = new InternalServerErrorException(routingCtx.failure().getLocalizedMessage());
			body = toResponseBody(internalServerErrorException.getError());
			routingCtx.response().setStatusCode(routingCtx.statusCode());
		}

		routingCtx.response()
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.endHandler(event -> LOGGER.info(Log.builder()
				.setContext(routingCtx.get(LoggerContextHandler.LOG_CONTEXT))
				.addMetadata("operation", "response")
				.build().toJson()))
			.end(body);
	}

	private static String toResponseBody(Error error) {
		var errors = new Error[] {error};
		var errorsMap = Map.of("errors", errors);

		return Json.encodePrettily(errorsMap);
	}

}
