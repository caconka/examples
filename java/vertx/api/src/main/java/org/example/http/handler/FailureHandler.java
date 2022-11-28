package org.example.http.handler;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.rxjava3.ext.web.RoutingContext;
import java.util.Arrays;
import java.util.Map;
import org.example.domain.exception.BackendException;
import org.example.domain.exception.BadRequestException;
import org.example.domain.exception.Error;
import org.example.domain.exception.InternalServerErrorException;
import org.example.domain.logger.Log;
import org.example.domain.logger.OperationType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class FailureHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FailureHandler.class);

	public void handle(RoutingContext routingCtx) {
		var err = routingCtx.failure();
		var body = err.getMessage();

		if (err instanceof BackendException exception) {
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

		var responseBody = body;
		var logMetadata = Map.of(
			"errorMsg", err.getMessage(),
			"detailMsg", err.getLocalizedMessage(),
			"cause", err.getCause() + "",
			"stackTrace", Arrays.toString(err.getStackTrace()));

		routingCtx.response()
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.endHandler(event -> LOGGER.info(Log.builder()
				.setContext(routingCtx.get(LoggerContextHandler.LOG_CONTEXT))
				.setOperation(OperationType.OUTPUT)
				.setMetadata(logMetadata)
				.setMessage("Error handling")
				.setResponseBody(responseBody).build().toJson()))
			.end(responseBody);
	}

	private static String toResponseBody(Error error) {
		var errors = new Error[] {error};
		var errorsMap = Map.of("errors", errors);

		return Json.encode(errorsMap);
	}

}
