package org.example.controller.handler;

import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.ext.web.RoutingContext;

public class HealthCheckHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext routingCtx) {
		routingCtx.response()
			.setStatusCode(HttpResponseStatus.OK.code())
			.putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
			.end(new JsonObject().put("status", "OK").encode());
	}

}
